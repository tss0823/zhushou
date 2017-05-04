package com.yuntao.zhushou.service.inter;

/**
 * Created by shan on 2017/5/2.
 */
public interface DeployService {

    void autoDeploy(String json);

    void autoDeployTask();

    /**
     * 改变自动发布状态
     * @param state false 结束，true 开始
     */
    void changeDeployState(boolean state);
}
