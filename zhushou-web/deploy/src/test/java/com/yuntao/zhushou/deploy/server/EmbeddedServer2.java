package com.yuntao.zhushou.deploy.server;

import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class EmbeddedServer2 {
    public static void main(String[] args) throws Exception {
//		System.setProperty("hongbaoAppType","proxy");
        java.security.Security.setProperty("networkaddress.cache.ttl" , "0");
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(1027);
        tomcat.setBaseDir("target/tomcat");
        tomcat.addWebapp("/", new File("/u01/workspace/zhushou/zhushou-web/deploy/src/main/webapp").getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}


