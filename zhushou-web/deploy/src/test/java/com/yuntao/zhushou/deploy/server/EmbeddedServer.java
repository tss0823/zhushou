package com.yuntao.zhushou.deploy.server;

import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class EmbeddedServer {
    public static void main(String[] args) throws Exception {
        // System.setProperty("hongbaoAppType","proxy");
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(28081);
        tomcat.setBaseDir("target/tomcat");
        String userDir = System.getProperty("user.dir");

        tomcat.addWebapp("/", new File(userDir+"/zhushou-web/deploy/src/main/webapp").getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}
