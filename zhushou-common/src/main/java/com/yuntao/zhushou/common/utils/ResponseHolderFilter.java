package com.yuntao.zhushou.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class ResponseHolderFilter implements Filter {

    private static Logger log = LoggerFactory.getLogger(ResponseHolderFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("res filter init...");
//        ServletContext sc = filterConfig.getServletContext();
//        WebApplicationContext beanFactory = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
//        ProxyContentService propertyPlaceholderConfigurer = (PropertySourcesPlaceholderConfigurer) beanFactory.getBean("propertyPlaceholderConfigurer");
//        //初始化config-xx.properties系统配置
//        PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = (PropertySourcesPlaceholderConfigurer) beanFactory.getBean("propertyPlaceholderConfigurer");
//        AppConfigUtils.init(propertyPlaceholderConfigurer.getAppliedPropertySources());

        //
        //代理服务启动



    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
//        log.info("brefore request ...");
        try {
            ResponseHolder.set(response);
            chain.doFilter(request, response);
        } finally {
            ResponseHolder.clear();
//            log.info("reponse clear1 ...");
        }
    }

    @Override
    public void destroy() {
//        log.error("reponse clear2 ...");
        ResponseHolder.clear();
    }

}
