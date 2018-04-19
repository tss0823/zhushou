package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ConfigType {

    system(0, "系统"),

    company(1, "公司"),

    project(2, "项目"),

    ;

    private int code;
    private String description;

    ConfigType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ConfigType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (ConfigType s : ConfigType.values()) {
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
