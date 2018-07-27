package com.yuntao.zhushou.dal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 等价于spring-config.xml
 */
@Configuration
public class ApplicationConfig {

    protected final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    /**
     * 线上环境的profile名称
     */
    public static final String PROFILE_NAME_PROD = "prod";

    @Autowired
    Environment env;


    @Value("${profiles.active}")
    private String active;

    @Value("${config.dir}")
    private String configDir;

    public boolean isProd() {
        return PROFILE_NAME_PROD.equals(active);
    }

    public boolean isTest() {
        return "test".equals(active);
    }

    /**
     * dev profile
     */
    @Profile("dev")
    @Bean(name = "propertyPlaceholderConfigurer")
    public  PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurerDev() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("filtered.properties");
        Properties p = new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        bisLog.info("active={}",active);
//        bisLog.info("configDir={}",configDir);
        String baseHome = p.getProperty("base.home");
        bisLog.info("baseHome={}",baseHome);

        String configPath = baseHome + File.separator + "conf";
        PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
//        String projectDir = System.getProperty("user.dir");
        String path = configPath + File.separator+"config.properties";
        bisLog.info("config path={}",path);

        ppc.setLocation(new FileSystemResource(path));
        bisLog.info("config.properties loaded");
        return ppc;
    }

    @Profile("test")
    @Bean(name = "propertyPlaceholderConfigurer")
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurerTest() {
        PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        ppc.setLocation(new ClassPathResource("env/config-test.properties"));
        bisLog.info("env/config-test.properties loaded");
        return ppc;
    }

    @Profile(PROFILE_NAME_PROD)
    @Bean(name = "propertyPlaceholderConfigurer")
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurerProd() {
        PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        String projectDir = System.getProperty("user.dir");
        String path = projectDir+ File.separator+"conf/config.properties";
        ppc.setLocation(new FileSystemResource(path));
        bisLog.info("config.properties loaded");
        return ppc;
    }

}