package com.yuntao.zhushou.common.http;

import java.util.List;
import java.util.Map;

/**
 * Created by shan on 2016/7/22.
 */
public class RequestRes {

    private String url;

    private Map<String,String> headers;

    private Map<String,String> params;

    private List<HttpParam> paramList;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public List<HttpParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<HttpParam> paramList) {
        this.paramList = paramList;
    }
}
