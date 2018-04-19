package com.yuntao.zhushou.service;

import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.service.inter.ProjectService;
import com.yuntao.zhushou.service.inter.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;

@TransactionConfiguration(defaultRollback = false)
public class ProjectServiceTest extends BaseServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;


    @Test
    public void testCreateTable() {
//        userService.auditNotPass(null);

        User user = userService.findById(8L);
        Long resultId = projectService.build(5L, user);
        System.out.printf("id="+resultId);

    }


}
