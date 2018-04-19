package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JavaDataType {


    STRING("java.lang.String","java.lang.String"),

    LONG("java.lang.Long","java.lang.Long"),

    INTEGER("java.lang.Integer","java.lang.Integer"),

    TIME("java.util.Date","java.util.Date"),

    DOUBLE("java.lang.Double","java.lang.Double"),

    BOOLEAN("java.lang.Boolean","java.lang.Boolean"),

    BYTE("java.lang.Byte","java.lang.Byte");


    ;

    private String code;
    private String description;

    JavaDataType(String code, String description) {
        this.code = code;
        this.description = description;
    }

//    public static JavaDataType getByCode(Integer code) {
//        if(code == null){
//            return null;
//        }
//        for (JavaDataType s : JavaDataType.values()) {
//            if (s.getCode() == code) {
//                return s;
//            }
//        }
//        return null;
//    }


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
