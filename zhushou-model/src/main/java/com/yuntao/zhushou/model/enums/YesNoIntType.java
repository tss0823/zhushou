package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum YesNoIntType {

    yes(1, "是"),

    no(0, "否"),  //



    ;

    private int code;
    private String description;

    YesNoIntType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static YesNoIntType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (YesNoIntType s : YesNoIntType.values()) {
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
