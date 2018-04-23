package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.Host;

import java.util.List;

/**
 * Created by shengshan.tang on 2015/12/26 at 22:13
 */
public class AppVo extends App {

    private String userName;

    private String lastTime;

    private String testVersion;

    private String prodVersion;

    private String testDeployVersion;

    private String prodDeployVersion;


    private List<Host> testHostList;

    private List<Host> prodHostList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getTestVersion() {
        return testVersion;
    }

    public void setTestVersion(String testVersion) {
        this.testVersion = testVersion;
    }

    public String getProdVersion() {
        return prodVersion;
    }

    public void setProdVersion(String prodVersion) {
        this.prodVersion = prodVersion;
    }

    public String getTestDeployVersion() {
        return testDeployVersion;
    }

    public void setTestDeployVersion(String testDeployVersion) {
        this.testDeployVersion = testDeployVersion;
    }

    public String getProdDeployVersion() {
        return prodDeployVersion;
    }

    public void setProdDeployVersion(String prodDeployVersion) {
        this.prodDeployVersion = prodDeployVersion;
    }

    public List<Host> getTestHostList() {
        return testHostList;
    }

    public void setTestHostList(List<Host> testHostList) {
        this.testHostList = testHostList;
    }

    public List<Host> getProdHostList() {
        return prodHostList;
    }

    public void setProdHostList(List<Host> prodHostList) {
        this.prodHostList = prodHostList;
    }
}
