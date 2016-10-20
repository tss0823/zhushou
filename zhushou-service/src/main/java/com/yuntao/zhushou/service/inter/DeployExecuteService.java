package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.User;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by shan on 2016/3/29.
 */
public interface DeployExecuteService {

    Queue<String> execMsgList = new ConcurrentLinkedQueue<>();

    void complie(User user, String appName, String branch, String model);

    void deploy(User user, String appName, String model, List<String> ipList);

    void deployStatic(User user, String appName, String model, List<String> ipList);

    void restart(User user, String appName, String model, List<String> ipList);

    void stop(User user, String appName, String model, List<String> ipList);

    void rollback(User user, String appName, String model, String backVer, List<String> ipList);

    List<String> branchList(String appName);
}
