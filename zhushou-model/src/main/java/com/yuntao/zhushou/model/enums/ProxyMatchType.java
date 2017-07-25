package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProxyMatchType {

    eq("eq", "等于"),

    neq("neq", "不等于"),

    include("include", "包含该"),

    exclude("exclude", "不包含"),

    regex("regex", "正则"),

    ;


    private String code;
    private String description;

    ProxyMatchType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ProxyMatchType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (ProxyMatchType s : ProxyMatchType.values()) {
            if (s.getCode().equals(code)) {
                return s;
            }
        }
        return null;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
