package com.yuntao.zhushou.zplugin;

import com.intellij.ide.util.PropertiesComponent;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.MD5Util;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.model.domain.User;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shan on 2017/9/8.
 */
public class ZpluginUtils {

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

    public static ResponseObject authCheck(){
        User user = ZpluginUtils.getAccountInfo();
        if(StringUtils.isBlank(user.getAccountNo()) || StringUtils.isBlank(user.getPwd())){
            throw new BizException("请先设置好账号和密码");
        }
        ResponseObject responseObject = CodeBuildUtils.login(user.getAccountNo(), MD5Util.MD5Encode(user.getPwd()));
        if(!responseObject.isSuccess()){
            throw new BizException(responseObject.getMessage());
        }

        return responseObject;
    }

}
