package com.yuntao.zhushou.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shan on 2016/7/31.
 */
public class WebContextUtils {

    public static HttpServletRequest getHttpRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public static HttpServletResponse getHttpResponse() {
        HttpServletResponse response = (HttpServletResponse) ResponseHolder.get();
        return response;
    }

    public static String getCookie(String key) {
        HttpServletRequest request = getHttpRequest();
        return getCookie(request.getCookies(), key);
    }

    private static String getCookie(Cookie[] cookies, String key) {
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (key.equalsIgnoreCase(cookie.getName()) && StringUtils.isNotBlank(cookie.getValue())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    public static  void setCookie(String key, String value, int expireTime) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        HttpServletResponse response = (HttpServletResponse) ResponseHolder.get();
        try {
            Cookie cookie = new Cookie(key, value);
            cookie.setMaxAge(expireTime);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
        }

    }
}
