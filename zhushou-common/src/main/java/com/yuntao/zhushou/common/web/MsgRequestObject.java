package com.yuntao.zhushou.common.web;


import com.yuntao.zhushou.common.constant.MsgConstant;

import java.io.Serializable;

/**
 * ws msg 请求消息对象
 * Created by shengshan.tang on 2015/12/14 at 21:00
 */
public class MsgRequestObject implements Serializable {

    private String key;

    private String type = MsgConstant.ReqResType.USER;  //

    private String bizType = MsgConstant.RequestBizType.NORMAL;  //

    private String message;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
