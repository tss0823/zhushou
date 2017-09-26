package com.yuntao.zhushou.zplugin;

import com.intellij.ide.util.PropertiesComponent;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.model.domain.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

/**
 * Created by shan on 2017/9/8.
 */
public class ZpluginUtils {
    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");
    protected final static Logger log = org.slf4j.LoggerFactory.getLogger(ZpluginUtils.class);
    public static String fixFilePath;

    public static void setAccountInfo(String accountNo,String pwd){
        PropertiesComponent.getInstance().setValue(ZpluginConstant.acountNo,accountNo);
        PropertiesComponent.getInstance().setValue(ZpluginConstant.pwd,pwd);
    }
    public static void setTestBranch(String testBranch){
        PropertiesComponent.getInstance().setValue(ZpluginConstant.testBranch,testBranch);
    }
    public static void setLogPath(String logPath){
        PropertiesComponent.getInstance().setValue(ZpluginConstant.logPath,logPath);
    }

    public static User getAccountInfo(){
        String accountNo = PropertiesComponent.getInstance().getValue(ZpluginConstant.acountNo);
        String pwd = PropertiesComponent.getInstance().getValue(ZpluginConstant.pwd);
        User user = new User();
        user.setAccountNo(accountNo);
        user.setPwd(pwd);
        return user;
    }

    public static String getTestBranch(){
        String value = PropertiesComponent.getInstance().getValue(ZpluginConstant.testBranch);
        return value;
    }

    public static String getLogPath(){
        String value = PropertiesComponent.getInstance().getValue(ZpluginConstant.logPath);
        return value;
    }
    public static void setWsLogPath(String wsLogPath){
        PropertiesComponent.getInstance().setValue(ZpluginConstant.wsLogPath,wsLogPath);
    }

    public static String getWsLogPath(){
        String value = PropertiesComponent.getInstance().getValue(ZpluginConstant.wsLogPath);
        return value;
    }
    public static void setUserInfo(String userInfo){
        PropertiesComponent.getInstance().setValue(ZpluginConstant.userInfo,userInfo);
    }

    public static String getUserInfo(){
        String value = PropertiesComponent.getInstance().getValue(ZpluginConstant.userInfo);
        return value;
    }

    public static void setValue(String key,String userInfo){
        PropertiesComponent.getInstance().setValue(key,userInfo);
    }

    public static String getValue(String key){
        String value = PropertiesComponent.getInstance().getValue(key);
        return value;
    }

    public static ResponseObject authCheck(){
        User user = ZpluginUtils.getAccountInfo();
        if(StringUtils.isBlank(user.getAccountNo()) || StringUtils.isBlank(user.getPwd())){
            throw new BizException("请先设置好账号和密码");
        }
        ResponseObject responseObject = ZhushouRpcUtils.login(user.getAccountNo(), user.getPwd());
        if(!responseObject.isSuccess()){
            throw new BizException(responseObject.getMessage());
        }

        return responseObject;
    }

    public static boolean isLogin(){
        User user = ZpluginUtils.getAccountInfo();
        if(StringUtils.isBlank(user.getAccountNo()) || StringUtils.isBlank(user.getPwd())){
            return false;
        }
        ResponseObject responseObject = ZhushouRpcUtils.login(user.getAccountNo(), user.getPwd());
        return responseObject.isSuccess();
    }

}
