package com.yuntao.zhushou.common.utils;

import javax.servlet.ServletResponse;


public class ResponseHolder<T> {


    private static ThreadLocal<ServletResponse> threadLocal = new ThreadLocal<ServletResponse>();

    public static void set(ServletResponse response) {

        threadLocal.set(response);
    }

    public static ServletResponse get() {

        return threadLocal.get();

    }

    public static void clear() {
        threadLocal.remove();
    }


}
