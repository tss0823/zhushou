package com.yuntao.zhushou.common.http;

import java.io.Serializable;

/**
 * Created by shan on 2017/5/4.
 */
public class HttpParam implements Serializable {

    private String key;

    private String value;

    public HttpParam(String key, Object value) {
        this.key = key;
        if(value != null){
            this.value = value.toString();
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
