package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * Created by shan on 2016/5/5.
 */
public class LogQuery extends BaseQuery {

    private Long projectId;

    private String model;

    private String appName;

    private String level;

    private Long userId;

    private Integer status;

    private String clientIp;

    private String stackId;

    private boolean isMaster;

    private String month;

    private String startTime;

    private String endTime;

    private String id;

    private Boolean showAll;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getStackId() {
        return stackId;
    }

    public void setStackId(String stackId) {
        this.stackId = stackId;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getShowAll() {
        return showAll;
    }

    public void setShowAll(Boolean showAll) {
        this.showAll = showAll;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
