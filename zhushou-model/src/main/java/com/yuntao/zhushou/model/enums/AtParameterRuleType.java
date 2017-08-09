package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AtParameterRuleType {

    //    0:静态，默认；1:数字，2：字符，3：返回值;4：接口中取
    statics(1, "静态"),

    integer(2, "整数"),

    decimal(3, "小数"),

    str(4, "字符"),

    result(5, "返回值"),

    inter(6, "接口"),

    ;

    private int code;
    private String description;

    AtParameterRuleType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AtParameterRuleType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (AtParameterRuleType s : AtParameterRuleType.values()) {
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
