package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AtParamterDataType {

    text("text", "文本"),

    file("file", "文件"),


    ;

    private String code;
    private String description;

    AtParamterDataType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AtParamterDataType getByCode(String code) {
        for (AtParamterDataType s : AtParamterDataType.values()) {
            if (s.getCode() == code) {
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
