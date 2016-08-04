package com.yuntao.zhushou.dal.annotation;

import com.yuntao.zhushou.model.enums.UserType;

@java.lang.annotation.Documented
@java.lang.annotation.Target(value = {java.lang.annotation.ElementType.METHOD})
@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface NeedLogin {
    String FORWARDURL_LOGIN = "/toLogin";
    String FORWARDURL_SYSTEM_BUSY = "/systemBusy";
    String FORWARDURL_ACCESS_LIMITED = "/accessLimited";


    String FORWARDURL_LOGIN_H5 = "/login.html";
    String FORWARDURL_SYSTEM_BUSY_H5 = "/systemBusy.html";
    String FORWARDURL_ACCESS_LIMITED_H5 = "/accessLimited.html";

    /**
     * 没有登录的时候转发的地址
     *
     * @return
     */
    String forwardUrl() default FORWARDURL_LOGIN;

    UserType[] accessUserType() default {UserType.ALL};
}
