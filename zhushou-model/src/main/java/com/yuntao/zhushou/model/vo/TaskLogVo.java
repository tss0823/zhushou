package com.yuntao.zhushou.model.vo;

import java.io.Serializable;

/**
 * Created by shengshan.tang on 9/15/2015 at 11:35 AM
 */
public class TaskLogVo implements Serializable {


    private String appName;  //应用名称 [shop,user,shop,trade]

    private String type;  //model [test,prod]

    private String module;  //类似模块 [红包过期,对账，红包核对]

    private String batchNo;  //任务批次号

    private String message;  //消息

    private String desc;  //详细消息

    private String time;  //开始执行时间

    private Long startTimeLong;

    private Long takeTime;  //任务消耗时间，如果是主体消息，则是所有消耗时间总和

    private String ip;  //任务机器IP，目前只有一台

    private boolean success;  //执行结果

    private String startTime;  //开始时间

    private String endTime;  //结束时间

    private boolean master;   //是否主体

    private String id; //生成的id ,es 生成id

    private String lastTime;

    private String status;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getStartTimeLong() {
        return startTimeLong;
    }

    public void setStartTimeLong(Long startTimeLong) {
        this.startTimeLong = startTimeLong;
    }

    public Long getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Long takeTime) {
        this.takeTime = takeTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
