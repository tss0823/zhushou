package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProxyReqFilterItemFilterType {

    req(0, "请求"),

    rewrite(1, "重写"),;


    private int code;
    private String description;

    ProxyReqFilterItemFilterType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ProxyReqFilterItemFilterType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProxyReqFilterItemFilterType s : ProxyReqFilterItemFilterType.values()) {
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
