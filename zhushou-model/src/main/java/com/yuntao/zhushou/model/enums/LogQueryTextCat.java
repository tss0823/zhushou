package com.yuntao.zhushou.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LogQueryTextCat {


    PARAMETERS("parameters", "请求参数"),

    RESPONSE("response", "返回结果"),

    MESSAGE("message", "消息"),

    REQ_HEADERS("reqHeaders", "请求头"),

    RES_HEADERS("resHeaders", "返回头"),



    ;

    private String code;
    private String description;

    LogQueryTextCat(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static LogQueryTextCat getByCode(String code) {
        for (LogQueryTextCat s : LogQueryTextCat.values()) {
            if (StringUtils.equals(code,s.getCode())) {
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
