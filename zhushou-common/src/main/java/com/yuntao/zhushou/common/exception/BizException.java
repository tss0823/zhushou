package com.yuntao.zhushou.common.exception;


import com.yuntao.zhushou.common.constant.AppConstant;

public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Object data;  //扩展数据
    private Integer code = AppConstant.ExceptionCode.NORMAL;   //编号

    public BizException(String message, Exception ex) {
        super(message,ex);
    }
    public BizException(String message) {
        super(message);
    }
    public BizException(String message, String data) {
        super(message);
        this.data = data;
    }
    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    public BizException(Integer code, String message, String data) {
        super(message);
        this.code = code;
        this.data = data;
    }


    public Object getData() {
        return data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
