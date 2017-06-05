package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WarnEventResultType {

    sms(0, "短信"),

    email(1, "邮箱"),  //

    weixin(2, "微信"),  //



    ;

    private int code;
    private String description;

    WarnEventResultType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static WarnEventResultType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (WarnEventResultType s : WarnEventResultType.values()) {
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
