package com.yuntao.zhushou.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LogQueryType {
    TERM("term", "词"),

    MATCH("match", "精准"),

    WILDCARD("wildcard", "通配"),

    REGEXP("regexp", "正则"),

    FUZZY("fuzzy", "模糊"),

    PREFIX("prefix", "前缀"),

    ;

    private String code;
    private String description;

    LogQueryType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static LogQueryType getByCode(String code) {
        for (LogQueryType s : LogQueryType.values()) {
            if (StringUtils.equals(code,s.getCode())) {
                return s;
            }
        }
        return null;
    }

    public static void checkCode(int code) {
        // UserType userType = getByCode(code);
        // if(userType==null){
        // throw new BizException("请检查用户类型");
        // }
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
