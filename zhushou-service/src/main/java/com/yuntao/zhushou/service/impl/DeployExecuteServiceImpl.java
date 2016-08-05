package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.model.constant.AppConstant;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.DeployLog;
import com.yuntao.zhushou.model.domain.Host;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.DeployExecuteService;
import com.yuntao.zhushou.service.inter.DeployLogService;
import com.yuntao.zhushou.service.inter.HostService;
import com.yuntao.zhushou.service.support.YTWebSocketServer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by shan on 2016/3/29.
 */
@Service
public class DeployExecuteServiceImpl extends AbstService implements DeployExecuteService {

    @Autowired
    private DeployLogService deployLogService;

    @Autowired
    private AppService appService;

    @Autowired
    private HostService hostService;

    @Autowired
    private YTWebSocketServer ytWebSocketServer;

    private Queue<String> deployLogQueue = new ConcurrentLinkedQueue<>();

    private List<String> newAppNameList = new ArrayList();

    private Map<String, String> modelMap = new HashMap<>();

    {
        modelMap.put("test", "测试");
        modelMap.put("prod", "线上");

        newAppNameList.add("search");
        newAppNameList.add("taskmq");
    }

    @Override
    public void complie(User user,String appName, String branch, String model) {
        App app = appService.findByName(appName);
        String codeName = app.getCodeName();
        String cmd = "sh /u01/deploy/script/deploy.sh package,"+codeName+"," + branch + "," + model;
        execShellScript(cmd, "complie");
    }

    @Override
    public void deploy(User user, String appName, String model, List<String> ipList) {
        App app = appService.findByName(appName);
        String codeName = app.getCodeName();
        String cmd = "sh /u01/deploy/script/deploy.sh run,"+codeName+" "+ appName + " " + StringUtils.join(ipList, ",");
        execShellScript(cmd, "deploy");
        saveLog(user, appName, model, "发布", ipList);

    }

    @Override
    public void deployStatic(User user, String appName, String model, List<String> ipList) {
        App app = appService.findByName(appName);
        String codeName = app.getCodeName();
        String cmd = "sh /u01/deploy/script/deploy.sh install,"+codeName+" "+ appName + " " + StringUtils.join(ipList, ",");
        if(newAppNameList.contains(appName)){  //is new version appName
        }
        execShellScript(cmd, "deployStatic");
        saveLog(user, appName, model, "发布静态", ipList);
    }

    @Override
    public void restart(User user, String appName, String model, List<String> ipList) {
        String cmd = "sh /u01/deploy/script/deploy.sh restart " + appName + " " + StringUtils.join(ipList, ",");
        execShellScript(cmd, "restart");
        saveLog(user, appName, model, "重启", ipList);
    }

    @Override
    public void stop(User user, String appName, String model, List<String> ipList) {
        String cmd = "sh /u01/deploy/script/deploy.sh stop " + appName + " " + StringUtils.join(ipList, ",");
        execShellScript(cmd, "stop");
        saveLog(user, appName, model, "下线", ipList);

    }

    @Override
    public void rollback(User user, String appName, String model, String backVer, List<String> ipList) {
        String cmd = "sh /u01/deploy/script/deploy.sh rollback," + backVer + " " + appName + " " + StringUtils.join(ipList, ",");
        execShellScript(cmd, "rollback");
        saveLog(user, appName, model, "回滚", ipList);
    }

    @Override
    public List<String> branchList(String appName) {
        App app = appService.findByName(appName);
        String codeName = app.getCodeName();
        String cmd = "sh /u01/deploy/script/branch_list.sh "+codeName;
        execShellScript(cmd, "branchList");
        String msg = null;
        Set<String> resultSet = new TreeSet<>();
        boolean isStart = false;
        while (!execMsgList.isEmpty()) {
            msg = execMsgList.poll();
            if (StringUtils.isEmpty(msg)) {
                continue;
            }
            msg = msg.trim();
            if (StringUtils.indexOf(msg, "endBranchList") != -1) {
                break;
            }
            if (isStart) {
                if (StringUtils.indexOf(msg, "remotes") == -1) {
                    continue;
                }
                if (StringUtils.indexOf(msg, "HEAD") != -1) {
                    continue;
                }
                if (StringUtils.indexOf(msg, "remotes") != -1) {
                    int index = msg.lastIndexOf("/");
                    msg = msg.substring(index + 1);
                }
                resultSet.add(msg);
            }
            if (StringUtils.startsWith(msg, "branchList")) {
                isStart = true;
            }
        }
        return new ArrayList<>(resultSet);
    }

    private void execShellScript(String cmd, String method) {
        //执行前先清空历史残留队里消息,for 浏览器 or client 可能中断执行
        clearExecMsg();
        if (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
            for (int i = 0; i < 5; i++) {
                offerExecMsg("windows 执行测试");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (method.equals("branchList")) {
                offerExecInnerMsg("branchList");
                offerExecInnerMsg("remotes/origin/cashCoupon");
                offerExecInnerMsg("remotes/origin/cashCoupon2");
                offerExecInnerMsg("remotes/origin/cashCoupon3");
                offerExecInnerMsg("remotes/origin/cashCoupon4");
                offerExecInnerMsg("remotes/origin/master");
                offerExecInnerMsg("remotes/origin/prod");
                offerExecInnerMsg("endBranchList");
            }
            offerExecMsg("execute success");
            return;
        }
        //linux
        Process process = null;
        BufferedReader reader = null;
        InputStream is = null;
        try {
            offerExecMsg(cmd);
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
            pb.redirectErrorStream(true);
            process = pb.start();
            is = process.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String result;
            while ((result = reader.readLine()) != null) {
                if (method.equals("compile") && result.indexOf("编译打包失败") != -1) {
                    offerExecMsg("exec-0");
                    throw new BizException("编译打包失败");
                }
                if(method.equals("branchList")){
                   offerExecInnerMsg(result);
                }else{
                    offerExecMsg(result);
                }
            }
            int errCode = process.waitFor();
            offerExecMsg("errCode=" + errCode);
            offerExecMsg("execute success");

        } catch (Exception e) {
            offerExecMsg("execute failed");
            throw new BizException("execute script failed", e);
        } finally {
            if (process != null) {
                process.destroy();
            }
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }

    }

    private void saveLog(User user, String appName, String model, String method, List<String> ipList) {
        //insert log
        List<Host> hostList = hostService.selectListByAll();
        List<String> hostNameList = new ArrayList(ipList.size());
        //get hostNameList
        for (String ip : ipList) {
            for (Host host : hostList) {
                if (StringUtils.equals(ip, host.getEth0())) {
                    hostNameList.add(host.getName());
                    break;
                }
            }
        }
        //end

        String content = user.getNickName() + "【" + method + "】了节点【 " + StringUtils.join(hostNameList, ",") + " 】";
        DeployLog deployLog = new DeployLog();
        deployLog.setAppName(appName);
        deployLog.setModel(model);
        deployLog.setContent(content);
        deployLog.setUserId(user.getId());
        deployLog.setUserName(user.getNickName());
        StringBuilder sb = new StringBuilder();
        StringBuilder backVerSb = new StringBuilder();
        String backVer = null;
        while (!deployLogQueue.isEmpty()) {
            String deployLogMsg = deployLogQueue.poll();
            //备份版本处理
            if (StringUtils.indexOf(deployLogMsg, "back_ver=") != -1) {
                String backVerStr = deployLogMsg.split("=")[1];
                if (backVer == null) {
                    backVer = backVerStr.split(",")[0];
                }
                String backVerIp = backVerStr.split(",")[1];
                backVerSb.append(backVerIp);
                backVerSb.append(",");
            }
            sb.append(deployLogMsg);
            sb.append("\r\n");

        }
        if (backVerSb.length() > 0) {
            deployLog.setBackVer(backVer + "," + backVerSb.delete(backVerSb.length() - 1, backVerSb.length()).toString());
        }
        deployLog.setLog(sb.toString().getBytes());
        deployLogService.insert(deployLog);

        //记录app log
        App app = new App();
        app.setName(appName);
        app.setLog(content);
        appService.updateByName(app);


    }

    private void offerExecMsg(String msg) {
        ytWebSocketServer.sendMessage(AppConstant.ResponseType.DEPLOY_SCRIPT,msg);
        deployLogQueue.offer(msg);
    }

    private void offerExecInnerMsg(String msg) {
        execMsgList.offer(msg); //为了拉分支用
    }

    private void clearExecMsg() {
        execMsgList.clear();
//        deployLogQueue.clear();
    }
}
