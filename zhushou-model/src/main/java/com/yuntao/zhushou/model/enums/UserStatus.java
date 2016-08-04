package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserStatus {
    INIT(0, "待审核"),

    PASS(1, "审核通过"),

    GOLD(2, "黄金会员"),

    PT(3, "铂金会员"),

    DM(3, "钻石会员"),

    NOT_PASS(-1, "审核不通过"),;

    private int code;
    private String description;

    UserStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UserStatus getByCode(int code) {
        for (UserStatus s : UserStatus.values()) {
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
