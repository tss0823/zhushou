package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.AppFront;

/**
 * Created by shengshan.tang on 2015/12/26 at 22:13
 */
public class AppFrontVo extends AppFront {

    private String userName;

    private String lastTime;

    private String testVersion;

    private String prodVersion;

    private String testDeployVersion;

    private String prodDeployVersion;

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
}
