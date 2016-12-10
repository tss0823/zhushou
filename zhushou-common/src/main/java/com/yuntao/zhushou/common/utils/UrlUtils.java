package com.yuntao.zhushou.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * Created by tangshengshan on 16-11-18.
 */
public class UrlUtils {

    public static String getUrl(String appName,String model,String domain,String queryUrl){
        String urlPrefix = appName;
        if(urlPrefix.equals("member")){  //特殊处理
            urlPrefix = "user";
        }
        if(StringUtils.equals(model,"test")){
            urlPrefix = "test."+urlPrefix;
        }
        String url = "http://"+urlPrefix+"."+domain;
        if(StringUtils.isNotBlank(queryUrl)){
            url = url + queryUrl;
        }
        return url;
    }

    public static String getDomain(URL url) {
        StringBuilder sb = new StringBuilder(url.getProtocol());
        sb.append("://");
        sb.append(url.getAuthority());
        if (sb.lastIndexOf("/") != sb.length()) {
            sb.append("/");
        }
        return sb.toString();
    }
}
