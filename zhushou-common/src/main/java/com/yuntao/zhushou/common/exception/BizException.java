package com.yuntao.zhushou.common.exception;


public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Object data;  //扩展数据

    public BizException(String message,Exception ex) {
        super(message,ex);
    }
    public BizException(String message) {
        super(message);
    }


    public Object getData() {
        return data;
    }
}
