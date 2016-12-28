package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserType {

    dev(1, "开发"),

    front(2, "前端"),

    test(3, "测试"),

    devOps(4, "运维"),

    operate(5, "运营"),

    ALL(0, "所有"),;


    private int code;
    private String description;

    UserType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UserType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (UserType s : UserType.values()) {
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
