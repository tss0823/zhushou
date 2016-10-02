package com.yuntao.zhushou.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.InputStream;
import java.util.*;

public class CustomizedPropertyConfigurer extends PropertyPlaceholderConfigurer {

    protected Logger log = LoggerFactory.getLogger(this.getClass());
    private static final Map<String, String> ctxPropertiesMap = new HashMap();

    public CustomizedPropertyConfigurer() {
    }

    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        try {
            super.processProperties(beanFactory, props);
        } catch (Exception var4) {
            this.log.error(var4.getMessage(), var4);
        }

        this.loadFromParam(props);
        this.loadFromSystemProp();
        this.loadFromFile();
        this.loadFromCommonFile();
        this.printProp();
    }

    private void printProp() {
        Set entrySet = ctxPropertiesMap.entrySet();

        String key;
        String value;
        for(Iterator it = entrySet.iterator(); it != null && it.hasNext(); this.log.warn("find property " + key + "=" + value)) {
            Map.Entry entry = (Map.Entry)it.next();
            key = (String)entry.getKey();
            value = (String)entry.getValue();
            if(StringUtils.containsIgnoreCase(key, "password")) {
                value = "******";
            }
        }

    }

    private void loadFromFile() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("env/config-" + (String)ctxPropertiesMap.get("spring.profiles.active") + ".properties");
        Properties p = new Properties();

        try {
            p.load(inputStream);
            Set e = p.stringPropertyNames();
            Iterator i$ = e.iterator();

            while(i$.hasNext()) {
                String name = (String)i$.next();
                String value = p.getProperty(name);
                this.putProp(name, value);
            }
        } catch (Exception var7) {
            this.log.error(var7.getMessage(), var7);
        }

    }

    private void loadFromCommonFile() {
        try {
            InputStream e = this.getClass().getClassLoader().getResourceAsStream("config-common.properties");
            Properties p = new Properties();
            p.load(e);
            Set stringPropertyNames = p.stringPropertyNames();
            Iterator i$ = stringPropertyNames.iterator();

            while(i$.hasNext()) {
                String name = (String)i$.next();
                String value = p.getProperty(name);
                this.putProp(name, value);
            }
        } catch (Exception var7) {
            this.log.error(var7.getMessage(), var7);
        }

    }

    private void loadFromSystemProp() {
        Properties properties = System.getProperties();
        Set propertyNames = properties.stringPropertyNames();
        if(propertyNames != null) {
            Iterator itPN = propertyNames.iterator();

            while(itPN != null && itPN.hasNext()) {
                String key = (String)itPN.next();
                this.putProp(key, properties.get(key) + "");
            }
        }

    }

    private void loadFromParam(Properties props) {
        Iterator i$ = props.keySet().iterator();

        while(i$.hasNext()) {
            Object key = i$.next();
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            this.putProp(keyStr, value);
        }

    }

    private void putProp(String key, String value) {
        ctxPropertiesMap.put(key, value);
    }

    public static String getContextProperty(String name) {
        return (String)ctxPropertiesMap.get(name);
    }

    public static String getModel() {
        return getContextProperty("spring.profiles.active");
    }

    public static boolean isProd() {
        return StringUtils.equals(getContextProperty("spring.profiles.active") + "", "prod");
    }

    public static boolean isTest() {
        return StringUtils.equals(getContextProperty("spring.profiles.active") + "", "test");
    }

    public static boolean isDev() {
        return StringUtils.equals(getContextProperty("spring.profiles.active") + "", "dev");
    }

}