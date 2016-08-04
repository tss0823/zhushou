package com.yuntao.zhushou.common.exception;


public class AuthException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String  code;


    public AuthException(String message, String code) {
        super(message);
        this.code = code;
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Exception ex) {
        super(message,ex);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
