package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProxyResRewriteResModel {

    statics(0, "静态"),

    direct(1, "跳转"),;

    private int code;
    private String description;

    ProxyResRewriteResModel(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ProxyResRewriteResModel getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProxyResRewriteResModel s : ProxyResRewriteResModel.values()) {
            if (s.getCode() == code) {
                return s;
            }
        }
        return null;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
