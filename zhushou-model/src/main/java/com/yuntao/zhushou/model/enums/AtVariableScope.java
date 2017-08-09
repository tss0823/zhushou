package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AtVariableScope {

    pri(0, "私有"),

    global(1, "全局"),


    ;

    private int code;
    private String description;

    AtVariableScope(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AtVariableScope getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (AtVariableScope s : AtVariableScope.values()) {
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
