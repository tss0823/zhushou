package com.yuntao.zhushou.service;

import com.yuntao.zhushou.service.inter.UserService;
import com.yuntao.zhushou.service.inter.WarnEventService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;

@TransactionConfiguration(defaultRollback = false)
public class WarnEventTest extends BaseServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private WarnEventService warnEventService;



    @Test
    public void testCreateTable() {
//        warnEventService.sendTask();
//        userService.auditNotPass(null);

    }


}
