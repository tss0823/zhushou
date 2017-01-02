package com.yuntao.zhushou.common.web;


import com.yuntao.zhushou.common.constant.AppConstant;
import com.yuntao.zhushou.common.constant.MsgConstant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * ws msg 返回消息对象
 * Created by shengshan.tang on 2015/12/14 at 21:00
 */
public class MsgResponseObject implements Serializable {

    private boolean success = true;

    private String key;  //企业key
    private Long userId;  //返回执行用户id
//    private String level = AppConstant.ResponseLevel.INFO;  //info,warn,error
    private String type = MsgConstant.ReqResType.USER;  //
    private String bizType = MsgConstant.RequestBizType.NORMAL;  //
    private String code = AppConstant.ResponseCode.NORMAL;   //系统定义code

    private String message;


    private Object data;



    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String,Object> put(String key, Object value) {
        if(this.data==null || !(this.data instanceof Map)){
            this.data = new HashMap<String,Object>();
        }
        Map<String,Object> map =  (Map<String, Object>) this.data;
        map.put(key, value);
        return map;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
