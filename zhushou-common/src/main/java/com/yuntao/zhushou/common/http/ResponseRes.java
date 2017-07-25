package com.yuntao.zhushou.common.http;

import java.util.Map;

/**
 * Created by shan on 2016/7/22.
 */
public class ResponseRes {

    private Map<String,String> headers;

    private byte [] result;

    private Integer status;

    private String bodyText;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public byte[] getResult() {
        return result;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }
}
