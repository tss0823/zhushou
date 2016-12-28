package com.yuntao.zhushou.web.task.config;

import com.yuntao.zhushou.dal.config.ApplicationConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 等价于spring-mvc.xml
 */
@Configuration
@Import(ApplicationConfig.class)
@ImportResource("classpath:applicationContext-web.xml")
@ComponentScan(basePackages = "com.yuntao.zhushou.web.task", includeFilters = {@Filter(RestController.class),
        @Filter(Component.class)}, useDefaultFilters = false)
public class WebMvcConfig extends WebMvcConfigurationSupport {



}