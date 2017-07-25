package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProxyFilterJoinType {

    and(0, "AND"),

    or(1, "OR"),;

    private int code;
    private String description;

    ProxyFilterJoinType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ProxyFilterJoinType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProxyFilterJoinType s : ProxyFilterJoinType.values()) {
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
