package com.yuntao.zhushou.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LogMesssageType {


    master(0, "master"),

    sql(1, "sql"),

    cache(2, "cache"),

    other(3, "other"),


    ;

    private int code;
    private String description;

    LogMesssageType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static LogMesssageType getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (LogMesssageType s : LogMesssageType.values()) {
            if (code == s.getCode()) {
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


    }
}
