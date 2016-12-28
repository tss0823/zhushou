package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.exception.AuthException;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.model.constant.AppConstant;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by shengshan.tang on 2015/11/25 at 18:57
 */
public class BaseController {

    protected final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    protected final static Logger log = org.slf4j.LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected UserService userService;

    @ExceptionHandler
    @ResponseBody
    public ResponseObject resolveException(HttpServletRequest request, Exception ex) {
        bisLog.error("system error",ex);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setSuccess(false);
        responseObject.setMessage(ex.getMessage());
        if( ex instanceof BizException){
            responseObject.setLevel(AppConstant.ResponseLevel.WARN);
        }else if( ex instanceof AuthException){
            AuthException authException  = (AuthException) ex;
            responseObject.setLevel(AppConstant.ResponseLevel.WARN);
            responseObject.setCode(authException.getCode());
        }else{
            responseObject.setLevel(AppConstant.ResponseLevel.ERROR);
            responseObject.setLevel(AppConstant.ResponseCode.SYSTEM_ERROR);
        }
        return responseObject;
    }

}
