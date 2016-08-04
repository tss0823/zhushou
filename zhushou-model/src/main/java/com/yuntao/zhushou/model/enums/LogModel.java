package com.yuntao.zhushou.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LogModel {


    TEST("test", "测试"),

    PROD("prod", "线上"),


    ;

    private String code;
    private String description;

    LogModel(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static LogQueryTextCat getByCode(String code) {
        for (LogQueryTextCat s : LogQueryTextCat.values()) {
            if (StringUtils.equals(code, s.getCode())) {
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


    }
}
