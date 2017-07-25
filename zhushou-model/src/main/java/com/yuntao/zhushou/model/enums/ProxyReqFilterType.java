package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProxyReqFilterType {

    url(0, "URL"),

    header(1, "Header"),

    body(2, "Body"),;

    private int code;
    private String description;

    ProxyReqFilterType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ProxyReqFilterType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProxyReqFilterType s : ProxyReqFilterType.values()) {
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
