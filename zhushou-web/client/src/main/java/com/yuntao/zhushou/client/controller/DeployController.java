package com.yuntao.zhushou.client.controller;

import com.yuntao.zhushou.client.support.CDWebSocketMsgHandler;
import com.yuntao.zhushou.common.constant.AppConstant;
import com.yuntao.zhushou.common.constant.MsgConstant;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.QiNiuTools;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.common.web.ShellExecObject;
import com.yuntao.zhushou.model.domain.AppVersion;
import com.yuntao.zhushou.model.enums.AppVerionStatus;
import com.yuntao.zhushou.model.enums.DeployLogType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("deploy")
public class DeployController extends BaseController {

//    @Autowired
//    private DeployExecuteService deployExecuteService;



//    @Autowired
//    private UserService userService;

    @Value("${model}")
    private String model;

    @Autowired
    private CDWebSocketMsgHandler cdWebSocketMsgHandler;

    private volatile String execModel = "prod";  //模式
    private volatile String execMessage = "nothing";  //操作
    private volatile boolean compileResult = true;


    private AtomicBoolean execRun = new AtomicBoolean(false);

//    @RequestMapping("getExecMsg")
////    @NeedLogin
//    public ResponseObject getExecMsg() {
//        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
//        int maxIndex = 5;
//        StringBuilder sb = new StringBuilder();
//        while(maxIndex > 0 && !deployExecuteService.execMsgList.isEmpty()){
//            sb.append(deployExecuteService.execMsgList.poll()+"\r\n");
//        }
//        responseObject.setData(sb.toString());
//        return responseObject;
//    }

    Queue<String> execMsgList = new ConcurrentLinkedQueue<>();


    private void offerExecMsg(String msg) {
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.SHELL,msg);
    }

    private void offerExecMsg(Long userId,String appName,String model,String method,List<String> ipList) {
        ShellExecObject shellExecObject = new ShellExecObject(appName, model, method, ipList);
        shellExecObject.setUserId(userId);
        String message = JsonUtils.object2Json(shellExecObject);
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.EVENT_END,message);
    }

    private void offerExecMsg(Long userId,String appName,String model,String method,String type) {
        ShellExecObject shellExecObject = new ShellExecObject(appName, model, method, type);
        shellExecObject.setUserId(userId);
        String message = JsonUtils.object2Json(shellExecObject);
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.EVENT_END,message);
    }

    private void offerExecInnerMsg(String msg) {
        execMsgList.offer(msg); //为了拉分支用
    }

    private void clearExecMsg() {
        execMsgList.clear();
        //TODO
//        deployLogQueue.clear();
    }

    private void execShellScript(String cmd, String method) {
        //执行前先清空历史残留队里消息,for 浏览器 or client 可能中断执行
        clearExecMsg();
        if (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
//            if (!StringUtils.equals(model,"test") ) {
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
                    offerExecMsg(result);
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

    @RequestMapping("branchList")
    public ResponseObject branchList(@RequestParam String codeName) {
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
        List<String> branchList = new ArrayList<>(resultSet);
//        List<String> branchList = deployExecuteService.branchList(appName);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(branchList);
        return responseObject;
    }





    @RequestMapping("compile")
//    @NeedLogin
    public ResponseObject compile(final @RequestParam String nickname,final @RequestParam String codeName,
                                  final @RequestParam String branch,final @RequestParam String model,final @RequestParam String compileProperty) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
//        final User user = userService.getCurrentUser();
        execModel = model;
        execMessage = nickname+"正在执行["+branch+"]["+execModel+"]编译操作，请稍候";
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,execMessage);
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.EVENT_START,"");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String cmd = "sh /u01/deploy/script/deploy.sh package,"+codeName+"," + branch + "," + model+","+"'"+compileProperty+"'";
                    execShellScript(cmd, "compile");
                    compileResult = true;
                }catch (Exception e){
                    compileResult = false;
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,"空闲");
                }
            }
        }).start();
        return responseObject;
    }

    @RequestMapping("autoCompile")
//    @NeedLogin
    public ResponseObject autoCompile(final @RequestParam Long userId,final @RequestParam String nickname,final @RequestParam String codeName,
                                      final @RequestParam String branch,final @RequestParam String model,final @RequestParam String compileProperty,
                                      final @RequestParam("appNames[]") List<String> appNames, final @RequestParam("ipList[]") List<String> ipsList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
//        final User user = userService.getCurrentUser();
        execModel = model;
        execMessage = nickname+"正在执行["+branch+"]["+execModel+"][自动]编译操作，请稍候";

        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,execMessage);
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.EVENT_START,"");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String cmd = "sh /u01/deploy/script/deploy.sh package,"+codeName+"," + branch + "," + model+","+"'"+compileProperty+"'";
                    execShellScript(cmd, "compile");
                    compileResult = true;
                    execRun.set(false);  //完成，恢复初始状态
                    //自动发布
                    for (int i = 0; i < appNames.size(); i++) {
                        String appName = appNames.get(i);
                        String ipStr = ipsList.get(i);
                        String[] ips = ipStr.split(",");
                        List<String> ipList = Arrays.asList(ips);
                        while (execRun.get()) {  //已经在运行
                            Thread.sleep(5000);
                        }
                        deploy(userId,nickname,appName,codeName,model,StringUtils.join(ipList,","));
                    }
                    //end
                }catch (Exception e){
                    compileResult = false;
                    throw new BizException("auto compile failed!",e);
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,"空闲");
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
//    @NeedLogin
    public ResponseObject deploy(final @RequestParam Long userId,@RequestParam String nickname, final @RequestParam String appName,final @RequestParam String codeName,
                                 final @RequestParam String model,final @RequestParam("ipList") String ipList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!compileResult){
            return responseObject;
        }
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
        execMessage = nickname+"正在执行["+appName+"]["+execModel+"]发布操作，请稍候";
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,execMessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String cmd = "sh /u01/deploy/script/deploy.sh run,"+codeName+" "+ appName + " " + ipList;
                    execShellScript(cmd, "deploy");
                }catch (Exception e){
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,"空闲");
                    offerExecMsg(userId,appName,model,"发布",Arrays.asList(ipList.split(",")));
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
    public ResponseObject deployStatic(final @RequestParam Long userId,@RequestParam String nickname, final @RequestParam String appName, final @RequestParam String codeName,
                                       final @RequestParam String model,final @RequestParam("ipList") String ipList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!compileResult){
            return responseObject;
        }
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
        execMessage = nickname+"正在执行["+appName+"]["+execModel+"]静态发布操作，请稍候";
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,execMessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String cmd = "sh /u01/deploy/script/deploy.sh install,"+codeName+" "+ appName + " " + ipList;
                    execShellScript(cmd, "deployStatic");
                }catch (Exception e){
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,"空闲");
                    offerExecMsg(userId,appName,model,"静态发布",Arrays.asList(ipList.split(",")));
                }
            }
        }).start();
        return responseObject;
    }

    @RequestMapping("stop")
    public ResponseObject stop(final @RequestParam Long userId,String nickname,final @RequestParam String appName,final @RequestParam String model,
                               final @RequestParam("ipList") String ipList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
        execMessage = nickname+"正在执行["+appName+"]["+execModel+"]下线操作，请稍候";
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,execMessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String cmd = "sh /u01/deploy/script/deploy.sh stop " + appName + " " + ipList;
                    execShellScript(cmd, "stop");
//                    deployExecuteService.stop(user,appName,model,ipList);
                }catch (Exception e){
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,"空闲");
                    offerExecMsg(userId,appName,model,"下线",Arrays.asList(ipList.split(",")));
                }
            }
        }).start();
        return responseObject;
    }

    @RequestMapping("start")
    public ResponseObject start(final @RequestParam Long userId,@RequestParam String nickname, final @RequestParam String appName,final @RequestParam String model,
                                final @RequestParam("ipList") String ipList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
        execMessage = nickname+"正在执行["+appName+"]["+execModel+"]上线操作，请稍候";
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,execMessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String cmd = "sh /u01/deploy/script/deploy.sh start " + appName + " " + ipList;
                    execShellScript(cmd, "restart");
                }catch (Exception e){
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,"空闲");
                    offerExecMsg(userId,appName,model,"上线",Arrays.asList(ipList.split(",")));
                }
            }
        }).start();
        return responseObject;
    }

    @RequestMapping("restart")
    public ResponseObject restart(final @RequestParam Long userId,@RequestParam String nickname, final @RequestParam String appName,final @RequestParam String model,
                                  final @RequestParam("ipList") String ipList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
        execMessage = nickname+"正在执行["+appName+"]["+execModel+"]重启操作，请稍候";
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,execMessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String cmd = "sh /u01/deploy/script/deploy.sh restart " + appName + " " + ipList;
                    execShellScript(cmd, "restart");
                }catch (Exception e){
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,"空闲");
                    offerExecMsg(userId,appName,model,"重启",Arrays.asList(ipList.split(",")));
                }
            }
        }).start();
        return responseObject;
    }

    @RequestMapping("rollback")
    public ResponseObject rollback(final @RequestParam Long userId,@RequestParam String nickname, final @RequestParam String appName,final @RequestParam String model,
                                   final @RequestParam String backVer, final @RequestParam("ipList") String ipList) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
        execMessage = nickname+"正在执行["+appName+"]["+execModel+"]回滚操作，请稍候";
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,execMessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String cmd = "sh /u01/deploy/script/deploy.sh rollback," + backVer + " " + appName + " " + ipList;
                    execShellScript(cmd, "rollback");
                }catch (Exception e){
                    throw e;
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,"空闲");
                    offerExecMsg(userId,appName,model,"回滚",Arrays.asList(ipList.split(",")));
                }
            }
        }).start();
        return responseObject;
    }


    @RequestMapping("deployFront")
    public ResponseObject deployFront(final @RequestParam Long userId,@RequestParam String nickname, final @RequestParam String appName,
                                     final @RequestParam String br,  final @RequestParam String model,final @RequestParam String type,
                                      final @RequestParam String version,final @RequestParam Long appVersionId ) {
        final ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        if(!execRun.compareAndSet(false,true)){
            responseObject.setSuccess(false);
            responseObject.setMessage(execMessage);
            return responseObject;
        }
        execMessage = nickname+"正在执行["+appName+"]["+model+"]前端发布"+type+"操作，请稍候";
        cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,execMessage);


        final String frontModel =  model.equals("prod") ? "release":"debug";
        final String outputPath = AppConstant.deploy.frontBuildPath+type+"/";
        String postfix = ".ipa";
        if(type.equals(DeployLogType.android.getDescription())){
            postfix = ".apk" ;
        }
        final  String fileName = appName+"_"+version+"_"+frontModel+postfix;
        final String appDownloadUrl = QiNiuTools.QINIU_DOMAIN + fileName;
        responseObject.setData(appDownloadUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean execState = true;
                try{
                    String cmd = "sh /u01/deploy/script/front/deploy_"+type+".sh " + appName + " " + br + " "+ frontModel + " " + version+" "+outputPath+" "+fileName;
                    execShellScript(cmd, "deployFront");

                    //上传app
                    String filePath = outputPath+fileName;
                    byte data[] = FileUtils.readFileToByteArray(new File(filePath));
                    QiNiuTools.uploadFileFixName(data, fileName);
                    offerExecMsg("upload file url="+appDownloadUrl);
                    //end

                }catch (Exception e){
                    execState = false;
                    throw new RuntimeException(e);
                }finally {
                    execRun.set(false);  //完成，恢复初始状态
                    cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.WARN,"空闲");

                    //发送消息执行完成，触发升级生效
                    AppVersion appVersion = new AppVersion();
                    appVersion.setId(appVersionId);
                    appVersion.setAppUrl(appDownloadUrl);
                    if(execState){
                        appVersion.setStatus(AppVerionStatus.online.getCode());
                    }else{
                        appVersion.setStatus(AppVerionStatus.error.getCode());
                    }
                    String appVersionJson = JsonUtils.object2Json(appVersion);
                    cdWebSocketMsgHandler.offerMsg(MsgConstant.ReqCoreBizType.FRONT_DEPLOY_END,appVersionJson);
                    //end

                    offerExecMsg(userId,appName,model,"发布",type);
                }
            }
        }).start();
        return responseObject;
    }




}
