package com.yuntao.zhushou.zplugin.sqlmap.bean;

/**
 * @author: shengshan.tang
 * @date: 2018/7/15 下午10:07
 */
public class SqlMapParam {

    private String name;

    private String type;

    public SqlMapParam(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
