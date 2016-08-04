package com.yuntao.zhushou.common.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParameterInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ((handler instanceof HandlerMethod) == false) {
            return true;
        }
//        LogUtils.logRequest(request);
//        HbLogContextMgr.appendReqInfo(request);
        return super.preHandle(request, response, handler);
    }
}
