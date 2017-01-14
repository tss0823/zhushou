package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum YesNoType {

    yes("true", "是"),

    no("false", "否"),  //



    ;

    private String code;
    private String description;

    YesNoType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static YesNoType getByCode(String code) {
        if(code == null){
            return null;
        }
        for (YesNoType s : YesNoType.values()) {
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
