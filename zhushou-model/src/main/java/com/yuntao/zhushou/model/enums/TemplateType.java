package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TemplateType {

    all(0, "系统"),

    increment(1, "增量"),


    ;

    private int code;
    private String description;

    TemplateType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static TemplateType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (TemplateType s : TemplateType.values()) {
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
