package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CacheType {

    str("str", "字符创"),  //

    hash("hash", "哈希"),  //

    byt("byt", "字节"),  //


    ;

    private String code;
    private String description;

    CacheType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static CacheType getByCode(String code) {
        if(code == null){
            return null;
        }
        for (CacheType s : CacheType.values()) {
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
