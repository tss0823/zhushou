package com.yuntao.zhushou.service.auth;

import com.yuntao.zhushou.common.exception.AuthException;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.constant.AppConstant;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.service.inter.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
public class AuthAspect {


    @Autowired
    private UserService userService;

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object aroundController(final ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        NeedLogin needLogin = method.getAnnotation(NeedLogin.class);
        if(needLogin != null){  //需要权限校验
            User user = userService.getCurrentUser();
            if(user == null){
                AuthException exception  = new AuthException("您已退出登录，请重新登录", AppConstant.ResponseCode.NOT_LOGIN);
                throw exception;
            }
        }
        //资源权限校验 TODO
        return joinPoint.proceed();
    }
}
