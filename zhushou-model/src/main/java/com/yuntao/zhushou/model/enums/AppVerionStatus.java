package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AppVerionStatus {

    ready(0, "就绪"),

    online(2, "启动"),

    offline(3, "下线"),

    error(4, "错误"),

;

    private int code;
    private String description;

    AppVerionStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AppVerionStatus getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (AppVerionStatus s : AppVerionStatus.values()) {
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
