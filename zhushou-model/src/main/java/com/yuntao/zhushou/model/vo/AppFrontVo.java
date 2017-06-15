package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.AppFront;

/**
 * Created by shengshan.tang on 2015/12/26 at 22:13
 */
public class AppFrontVo extends AppFront {

    private String userName;

    private String lastTime;

    private String testAndroidVersion;

    private String prodAndroidVersion;

    private String testIOSVersion;

    private String prodIOSVersion;

    private String testAndroidDeployVersion;

    private String prodAndroidDeployVersion;

    private String testIOSDeployVersion;

    private String prodIOSDeployVersion;

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

    public String getTestAndroidVersion() {
        return testAndroidVersion;
    }

    public void setTestAndroidVersion(String testAndroidVersion) {
        this.testAndroidVersion = testAndroidVersion;
    }

    public String getProdAndroidVersion() {
        return prodAndroidVersion;
    }

    public void setProdAndroidVersion(String prodAndroidVersion) {
        this.prodAndroidVersion = prodAndroidVersion;
    }

    public String getTestIOSVersion() {
        return testIOSVersion;
    }

    public void setTestIOSVersion(String testIOSVersion) {
        this.testIOSVersion = testIOSVersion;
    }

    public String getProdIOSVersion() {
        return prodIOSVersion;
    }

    public void setProdIOSVersion(String prodIOSVersion) {
        this.prodIOSVersion = prodIOSVersion;
    }

    public String getTestAndroidDeployVersion() {
        return testAndroidDeployVersion;
    }

    public void setTestAndroidDeployVersion(String testAndroidDeployVersion) {
        this.testAndroidDeployVersion = testAndroidDeployVersion;
    }

    public String getProdAndroidDeployVersion() {
        return prodAndroidDeployVersion;
    }

    public void setProdAndroidDeployVersion(String prodAndroidDeployVersion) {
        this.prodAndroidDeployVersion = prodAndroidDeployVersion;
    }

    public String getTestIOSDeployVersion() {
        return testIOSDeployVersion;
    }

    public void setTestIOSDeployVersion(String testIOSDeployVersion) {
        this.testIOSDeployVersion = testIOSDeployVersion;
    }

    public String getProdIOSDeployVersion() {
        return prodIOSDeployVersion;
    }

    public void setProdIOSDeployVersion(String prodIOSDeployVersion) {
        this.prodIOSDeployVersion = prodIOSDeployVersion;
    }
}
