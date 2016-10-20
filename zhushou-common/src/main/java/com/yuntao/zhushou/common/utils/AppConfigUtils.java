package com.yuntao.zhushou.common.utils;

import org.springframework.core.env.PropertySources;

import java.util.Properties;

/**
 * Created by shan on 2016/8/6.
 */
public class AppConfigUtils {
    private static Properties properties;
    public static void init(PropertySources propertySources){
        properties = (Properties) propertySources.get("localProperties").getSource();

    }
    public static String getValue(String name){
        return properties.getProperty(name);
    }
}
