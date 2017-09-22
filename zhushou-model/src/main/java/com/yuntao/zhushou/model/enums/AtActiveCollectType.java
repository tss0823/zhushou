package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AtActiveCollectType {

    ids(0, "ID"),

    time(1, "时间"),

    ;



    private int code;
    private String description;

    AtActiveCollectType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AtActiveCollectType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (AtActiveCollectType s : AtActiveCollectType.values()) {
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
