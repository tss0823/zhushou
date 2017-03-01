package com.yuntao.zhushou.service.auth;

import com.yuntao.zhushou.common.exception.AuthException;
import com.yuntao.zhushou.common.profiler.ProfileTaskManger;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.common.constant.AppConstant;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.service.inter.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Aspect
@Component
public class AuthAspect {

    /**
     * 缺省监测值为100毫秒，超过这个值的request请求将被记录
     */
    private int threshold = 100;

    @Autowired
    private UserService userService;

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object aroundController(final ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //start profile
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ProfileTaskManger.startFirst("", request.getRequestURL().toString());
        //end
        try {
            NeedLogin needLogin = method.getAnnotation(NeedLogin.class);
            if (needLogin != null) {  //需要权限校验
                User user = userService.getCurrentUser();
                if (user == null) {
                    AuthException exception = new AuthException("您已退出登录，请重新登录", AppConstant.ResponseCode.NOT_LOGIN);
                    throw exception;
                }
            }
            //资源权限校验 TODO
            return joinPoint.proceed();
        } finally {
            //start profile
            ProfileTaskManger.endLast(threshold);
            ProfileTaskManger.clear();
            //end
        }
    }

    public static void test(){
        try{
            if(true){
//                throw new RuntimeException("my");
            }
            Integer i = 1/ 0;
            System.out.printf(i.toString());
        }finally {
            System.out.printf("end");
        }
    }
    public static void main(String[] args) {
        test();
    }
}
