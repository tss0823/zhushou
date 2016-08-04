package com.yuntao.zhushou.service;

import com.yuntao.zhushou.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;

@TransactionConfiguration(defaultRollback = false)
public class UserServiceTest extends BaseServiceTest {

    @Autowired
    private UserService userService;



    //@Test
    public void testCreateTable() {
//        userService.auditNotPass(null);

    }


}
