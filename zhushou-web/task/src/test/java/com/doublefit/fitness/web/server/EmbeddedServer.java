package com.yuntao.zhushou.web.server;

import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class EmbeddedServer {
    public static void main(String[] args) throws Exception {
        // System.setProperty("hongbaoAppType","proxy");
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(38002);
        tomcat.setBaseDir("target/tomcat");
        tomcat.addWebapp("/", new File("src/main/webapp").getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}
