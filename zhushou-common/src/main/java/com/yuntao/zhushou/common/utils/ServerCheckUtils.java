package com.yuntao.zhushou.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by shan on 2017/6/10.
 */
public class ServerCheckUtils {

    public static String checkStatus(String hostAddr,Integer port){
        String checkUrl = "http://" + hostAddr + ":" + port+ "/checkServerStatus";
        String result;
        try {
            List<String> lines = HttpUtils.reqGet(checkUrl,6000,10000);
            result = StringUtils.join(lines, ",");
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }
}
