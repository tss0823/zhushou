package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.constant.AppConstant;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.DeployExecuteService;
import com.yuntao.zhushou.service.inter.UserService;
import com.yuntao.zhushou.service.support.YTWebSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("deploy")
public class DeployController extends BaseController {

    @Autowired
    private DeployExecuteService deployExecuteService;



    @Autowired
    private UserService userService;

    @Autowired
    private YTWebSocketServer ytWebSocketServer;

    private volatile String execModel = "prod";  //模式
    private volatile String execMessage = "nothing";  //操作
    private volatile boolean compileResult = true;

    private AtomicBoolean execRun = new AtomicBoolean(false);

    @RequestMapping("getExecMsg")
    @NeedLogin
    public ResponseObject getExecMsg() {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        int maxIndex = 5;
        StringBuilder sb = new StringBuilder();
        while(maxIndex > 0 && !deployExecuteService.execMsgList.isEmpty()){
            sb.append(deployExecuteService.execMsgList.poll()+"\r\n");
        }
        responseObject.setData(sb.toString());
        return responseObject;
    }

    @RequestMapping("branchList")
    @NeedLogin
    public ResponseObject branchList(@RequestParam String appName) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<String> branchList = deployExecuteService.branchList(appName);
        responseObject.setData(branchList);
        return responseObject;
    }


    @RequestMapping("complie")
    @NeedLogin
    public ResponseObject complie(final @RequestParam String appName,final @RequestParam String branch,final @RequestParam String model) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
        final User user = userService.getCurrentUser();
        execModel = model;
        execMessage = user.getNickName()+"正在执行["+branch+"]["+execModel+"]编译操作，请稍候";

        ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,execMessage);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    deployExecuteService.complie(user,appName,branch,model);
                    compileResult = true;
                }catch (Exception e){
                    compileResult = false;
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,"空闲");
                }
            }
        }).start();
        return responseObject;
    }

    /**
     * 发布
     * 流程控制，1：对应正确是编译模式，不能出现编译test,prod 发布
     * 2：不能跟别的发布流程冲突，比如，编译中，或者重启中
     * @param appName
     * @param ipList
     * @return
     */
    @RequestMapping("deploy")
    @NeedLogin
    public ResponseObject deploy(final @RequestParam String appName,final @RequestParam String model,final @RequestParam("ipList[]") List<String> ipList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!StringUtils.equals(execModel,model)){
            responseObject.setSuccess(false);
            responseObject.setMessage("发布模式不对，当前模式["+execModel+"],期待模式["+model+"],请先编译");
            return responseObject;
        }
        if(!compileResult){
            responseObject.setSuccess(false);
            responseObject.setMessage("编译打包失败，请先编译好再发布");
            return responseObject;
        }
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
        final User user = userService.getCurrentUser();
        execMessage = user.getNickName()+"正在执行["+appName+"]["+execModel+"]发布操作，请稍候";
        ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,execMessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    deployExecuteService.deploy(user,appName,model,ipList);
                }catch (Exception e){
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,"空闲");
                }
            }
        }).start();
        return responseObject;
    }

    /**
     * 静态发布
     * 流程控制，1：对应正确是编译模式，不能出现编译test,prod 发布
     * 2：不能跟别的发布流程冲突，比如，编译中，或者重启中
     * @param appName
     * @param ipList
     * @return
     */
    @RequestMapping("deployStatic")
    @NeedLogin
    public ResponseObject deployStatic(final @RequestParam String appName,final @RequestParam String model,final @RequestParam("ipList[]") List<String> ipList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!StringUtils.equals(execModel,model)){
            responseObject.setSuccess(false);
            responseObject.setMessage("发布模式不对，当前模式["+execModel+"],期待模式["+model+"],请先编译");
            return responseObject;
        }
        if(!compileResult){
            responseObject.setSuccess(false);
            responseObject.setMessage("编译打包失败，请先编译好再发布");
            return responseObject;
        }
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
        final User user = userService.getCurrentUser();
        execMessage = user.getNickName()+"正在执行["+appName+"]["+execModel+"]静态发布操作，请稍候";
        ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,execMessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    deployExecuteService.deployStatic(user,appName,model,ipList);
                }catch (Exception e){
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,"空闲");
                }
            }
        }).start();
        return responseObject;
    }

    @RequestMapping("stop")
    @NeedLogin
    public ResponseObject stop(final @RequestParam String appName,final @RequestParam String model,final @RequestParam("ipList[]") List<String> ipList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
        final User user = userService.getCurrentUser();
        execMessage = user.getNickName()+"正在执行["+appName+"]["+execModel+"]下线操作，请稍候";
        ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,execMessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    deployExecuteService.stop(user,appName,model,ipList);
                }catch (Exception e){
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,"空闲");
                }
            }
        }).start();
        return responseObject;
    }

    @RequestMapping("restart")
    @NeedLogin
    public ResponseObject restart(final @RequestParam String appName,final @RequestParam String model,final @RequestParam("ipList[]") List<String> ipList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
        final User user = userService.getCurrentUser();
        execMessage = user.getNickName()+"正在执行["+appName+"]["+execModel+"]重启操作，请稍候";
        ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,execMessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    deployExecuteService.restart(user,appName,model,ipList);
                }catch (Exception e){
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,"空闲");
                }
            }
        }).start();
        return responseObject;
    }

    @RequestMapping("rollback")
    @NeedLogin
    public ResponseObject rollback(final @RequestParam String appName,final @RequestParam String model,
                                   final @RequestParam String backVer,final @RequestParam("ipList[]") List<String> ipList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
        final User user = userService.getCurrentUser();
        execMessage = user.getNickName()+"正在执行["+appName+"]["+execModel+"]回滚操作，请稍候";
        ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,execMessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    deployExecuteService.rollback(user,appName,model,backVer,ipList);
                }catch (Exception e){
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    ytWebSocketServer.sendMessage(AppConstant.ResponseType.MONITOR_STATUS,"空闲");
                }
            }
        }).start();
        return responseObject;
    }



}
