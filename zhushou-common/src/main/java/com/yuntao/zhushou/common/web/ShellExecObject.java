package com.yuntao.zhushou.common.web;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangshengshan on 17-1-1.
 */
public class ShellExecObject implements Serializable{

    private String appName;
    private Long userId;
    private String model;
    /**
     * 是否发布前端
     */
    private boolean deployFront;
    /**
     * android or ios
     */
    private String type;
    String method;
    List<String> ipList;

    public ShellExecObject() {
    }

    public ShellExecObject(String appName, String model, String method, List<String> ipList) {
        this.deployFront = false;
        this.appName = appName;
        this.model = model;
        this.method = method;
        this.ipList = ipList;
    }
    public ShellExecObject(String appName, String model, String method, String type) {
        this.deployFront = true;
        this.appName = appName;
        this.model = model;
        this.method = method;
        this.type = type;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getIpList() {
        return ipList;
    }

    public void setIpList(List<String> ipList) {
        this.ipList = ipList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean  getDeployFront() {
        return deployFront;
    }

    public void setDeployFront(boolean deployFront) {
        this.deployFront = deployFront;
    }
}
