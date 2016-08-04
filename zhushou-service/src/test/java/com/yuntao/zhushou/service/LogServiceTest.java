package com.yuntao.zhushou.service;

import com.yuntao.zhushou.service.inter.LogService;
import com.yuntao.zhushou.service.inter.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;

@TransactionConfiguration(defaultRollback = false)
public class LogServiceTest extends BaseServiceTest {

    @Autowired
    private LogService logService;



    @Test
    public void test1() {
        logService.delHisDataTask();
//        userService.auditNotPass(null);

    }


}
