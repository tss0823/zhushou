package com.yuntao.zhushou.model.query;

/**
 * Created by shan on 2016/3/27.
 */
public class DeployLogQuery extends BaseQuery{

    private String appName;

    private String model;

    private String userName;

    private String deployTimeStart;

    private String deployTimeEnd;

    private String backVerLike;

    private  Integer topNum;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeployTimeStart() {
        return deployTimeStart;
    }

    public void setDeployTimeStart(String deployTimeStart) {
        this.deployTimeStart = deployTimeStart;
    }

    public String getDeployTimeEnd() {
        return deployTimeEnd;
    }

    public void setDeployTimeEnd(String deployTimeEnd) {
        this.deployTimeEnd = deployTimeEnd;
    }

    public String getBackVerLike() {
        return backVerLike;
    }

    public void setBackVerLike(String backVerLike) {
        this.backVerLike = backVerLike;
    }

    public Integer getTopNum() {
        return topNum;
    }

    public void setTopNum(Integer topNum) {
        this.topNum = topNum;
    }
}
