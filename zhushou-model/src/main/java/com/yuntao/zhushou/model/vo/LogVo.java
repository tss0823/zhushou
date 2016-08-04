package com.yuntao.zhushou.model.vo;

import java.io.Serializable;

/**
 * Created by shan on 2016/5/5.
 */
public class LogVo implements Serializable {

    private String stackId;

    private String url;

    private Long userId;

    private String mobile;

    private String time;

    private String message;

    private String ip;

    private String appName;

    private int status;

    private String startTime;

    private String endTime;

    private String type;  //model

    private boolean master;

    private String lastTime;

    private Long takeTime;

    //log v2.0 添加属性
    private String clientIp;

    private String userAgent;

    private String reqContentLen;

    private String resContentLen;

    private String reqUrl;

    private String reqHeaders;

    private String resHeaders;

    private String parameters;

    private String response;

    private String level;  //级别

    private String name;  //用户名称

    //扩展自定义的
    private String index;
    private String id;


    public String getStackId() {
        return stackId;
    }

    public void setStackId(String stackId) {
        this.stackId = stackId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }


    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getReqContentLen() {
        return reqContentLen;
    }

    public void setReqContentLen(String reqContentLen) {
        this.reqContentLen = reqContentLen;
    }

    public String getResContentLen() {
        return resContentLen;
    }

    public void setResContentLen(String resContentLen) {
        this.resContentLen = resContentLen;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(String reqHeaders) {
        this.reqHeaders = reqHeaders;
    }

    public String getResHeaders() {
        return resHeaders;
    }

    public void setResHeaders(String resHeaders) {
        this.resHeaders = resHeaders;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Long takeTime) {
        this.takeTime = takeTime;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
