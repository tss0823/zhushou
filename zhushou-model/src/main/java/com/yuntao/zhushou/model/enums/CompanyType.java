package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CompanyType {

    pri(0, "私有"),

    pub(1, "公共"),

    ;

    private int code;
    private String description;

    CompanyType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static CompanyType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (CompanyType s : CompanyType.values()) {
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
