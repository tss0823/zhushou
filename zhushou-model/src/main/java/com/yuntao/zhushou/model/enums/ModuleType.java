package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ModuleType {

    user("user", "用户"),  //用户，基础模块

    sign("sign", "打卡"),  //

    stats("stats", "统计"),  //

    proxy("proxy", "代理商"),

    biz("biz", "用户"),

    trade("trade", "交易"),

    pay("pay", "支付"),

    search("search", "搜索"),


    ;

    private String code;
    private String description;

    ModuleType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ModuleType getByCode(String code) {
        for (ModuleType s : ModuleType.values()) {
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
