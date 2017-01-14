package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IdocUrlType {

    inters(0, "接口文档"),

    enums(1, "枚举"),

    res(2, "资源"),

;

    private int code;
    private String description;

    IdocUrlType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static IdocUrlType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (IdocUrlType s : IdocUrlType.values()) {
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
