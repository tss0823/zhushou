package com.yuntao.zhushou.common.web;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangshengshan on 17-1-1.
 */
public class ShellExecObject implements Serializable{

    private String appName;
    private String model;
    String method;
    List<String> ipList;

    public ShellExecObject() {
    }

    public ShellExecObject(String appName, String model, String method, List<String> ipList) {
        this.appName = appName;
        this.model = model;
        this.method = method;
        this.ipList = ipList;
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
}
