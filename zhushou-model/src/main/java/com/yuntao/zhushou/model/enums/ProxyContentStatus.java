package com.yuntao.zhushou.model.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProxyContentStatus {

    success(0, "成功"),

    error(1, "失败"),

    timeout(2, "超时"),

;

    private int code;
    private String description;

    ProxyContentStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ProxyContentStatus getByCode(Integer code) {
        if(code == null){
            return null;
        }
        for (ProxyContentStatus s : ProxyContentStatus.values()) {
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
