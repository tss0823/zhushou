package com.yuntao.zhushou.model.param;

import java.io.Serializable;

/**
 * Created by shan on 2016/8/17.
 */
public class DataMap implements Serializable {

    private String key;

    private String value;

    public DataMap() {
    }

    public DataMap(String key, String value) {
        this.key = key;
        this.value = value;
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
