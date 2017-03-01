package com.yuntao.zhushou.common.profiler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class PerformanceLogAspect {




//    @Pointcut("execution(* com.yuntao.zhushou.deploy.controller.*.*(..))")
//    public void auth(){
//
//    }

//    @Around("auth()")
//    public void beforeExecute(JoinPoint joinPoint){
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        ProfileTaskManger.startFirst("", request.getRequestURL().toString());
//
//        NeedLogin needLogin = method.getAnnotation(NeedLogin.class);
//        if(needLogin != null){  //需要权限校验
//            User user = userService.getCurrentUser();
//            if(user == null){
//                AuthException exception  = new AuthException("您已退出登录，请重新登录", AppConstant.ResponseCode.NOT_LOGIN);
//                throw exception;
//            }
//        }
//    }

//    @After("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
//    public void afterExecute(JoinPoint joinPoint){
//        ProfileTaskManger.endLast(threshold);
//        ProfileTaskManger.clear();
//    }

    @Around("execution(* com..service.impl.*.*(..)) ")
    public Object executePerLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        long time = System.currentTimeMillis();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String clsName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String actionMsg = clsName + "." + methodName;

        ProfileTaskManger.start(actionMsg, "");
        try {
            return joinPoint.proceed();
        } finally {
            ProfileTaskManger.end();
        }
//        Object resultObj = joinPoint.proceed();
//        long takeTime = System.currentTimeMillis() - time;
//        String message = actionMsg + ",takeTime " + takeTime + " ms";
//        if (takeTime > 50 && takeTime <= 100) {
//            performanceLog.error(message);
//        } else if(takeTime > 100){
//            performanceLog.fatal(message);
//        }
//        if (getMonitor()) {
//            Method method = invocation.getMethod();
//            String className = ClassUtils.getShortClassName(method.getDeclaringClass());
//            ProfileTaskManger.start(className, className + ":" + method.getName());
//        }
//        try {
//            return invocation.proceed();
//        } finally {
//            if (getMonitor()) {
//                ProfileTaskManger.end(this);
//            }
//        }
//        return resultObj;
    }
}
