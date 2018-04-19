/**
 * 
 */
package com.yuntao.zhushou.common.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author tangss
 * @2013年9月2日 @上午9:53:22
 */
public class DbTypeMysqlUtils {

    private static Properties prop;
    static {
        Resource resource = new ClassPathResource("db-type-mysql.properties");
        try {
            prop = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return prop.getProperty(key);
    }

    public static String getValue(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }

}
