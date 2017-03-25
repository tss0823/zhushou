package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeployLogType {

    server(0, "后台"),

    android(1, "Android"),

    ios(2, "IOS"),

;

    private int code;
    private String description;

    DeployLogType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static DeployLogType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (DeployLogType s : DeployLogType.values()) {
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
