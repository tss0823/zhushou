package com.yuntao.zhushou.model.web;

import com.yuntao.zhushou.model.constant.AppConstant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * web返回给前端对象
 * Created by shengshan.tang on 2015/12/14 at 21:00
 */
public class ResponseObject implements Serializable {

    private boolean success = true;

    private String level = AppConstant.ResponseLevel.INFO;  //info,warn,error
    private String type = AppConstant.ResponseType.NORMAL;  //

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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
}
