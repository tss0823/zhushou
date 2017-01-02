package com.yuntao.zhushou.deploy.support;

import com.yuntao.zhushou.service.support.deploy.DZMessageHelperServer;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by shan on 2016/8/19.
 */
@Component
public class MessageBootServer implements InitializingBean {


    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    private final static Logger log = org.slf4j.LoggerFactory.getLogger(MessageBootServer.class);

    @Autowired
    private DZMessageHelperServer dzMessageHelperServer;


    @Override
    public void afterPropertiesSet() throws Exception {
        dzMessageHelperServer.start();
    }
}
