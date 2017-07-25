package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProxyResRewriteResType {

    header(0, "Header"),

    body(1, "Body"),;

    private int code;
    private String description;

    ProxyResRewriteResType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ProxyResRewriteResType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProxyResRewriteResType s : ProxyResRewriteResType.values()) {
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
