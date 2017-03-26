package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.AppVersion;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.enums.AppVerionStatus;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.AppVersionService;
import com.yuntao.zhushou.service.inter.CompanyService;
import com.yuntao.zhushou.service.inter.UserService;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("deploy")
public class DeployController extends BaseController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AppService appService;


    @Autowired
    private UserService userService;

    @Autowired
    private AppVersionService appVersionService;


    @RequestMapping("branchList")
    @NeedLogin
    public ResponseObject branchList(@RequestParam String appName) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findByName(companyId, appName);

        //get compnay
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/branchList");
        Map<String,String> params = new HashMap<>();
        params.put("codeName",app.getCodeName());
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }


    @RequestMapping("complie")
    @NeedLogin
    public ResponseObject complie(final @RequestParam String appName,final @RequestParam String branch,final @RequestParam String model) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findByName(companyId, appName);

        //get compnay
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/complie");
        Map<String,String> params = new HashMap<>();
        params.put("nickname",user.getNickName());
        params.put("codeName",app.getCodeName());
        params.put("branch",branch);
        params.put("model",model);
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
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
    public ResponseObject deploy(final @RequestParam String appName,final @RequestParam String model,
                                 final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findByName(companyId, appName);

        //get compnay
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/deploy");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",appName);
        params.put("codeName",app.getCodeName());
        params.put("model",model);
        for (String ip : ipList) {
            params.put("ipList[]",ip);
        }
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
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
    public ResponseObject deployStatic(final @RequestParam String appName,final @RequestParam String model,
                                       final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findByName(companyId, appName);

        //get compnay
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/deployStatic");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",appName);
        params.put("codeName",app.getCodeName());
        params.put("model",model);
        for (String ip : ipList) {
            params.put("ipList[]",ip);
        }
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }

    @RequestMapping("stop")
    @NeedLogin
    public ResponseObject stop(final @RequestParam String appName,final @RequestParam String model,final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findByName(companyId, appName);

        //get compnay
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/stop");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",appName);
        params.put("model",model);
        for (String ip : ipList) {
            params.put("ipList[]",ip);
        }
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }

    @RequestMapping("restart")
    @NeedLogin
    public ResponseObject restart(final @RequestParam String appName,final @RequestParam String model,final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findByName(companyId, appName);

        //get compnay
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/restart");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",appName);
        params.put("model",model);
        for (String ip : ipList) {
            params.put("ipList[]",ip);
        }
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }

    @RequestMapping("rollback")
    @NeedLogin
    public ResponseObject rollback(final @RequestParam String appName,final @RequestParam String model, final @RequestParam String backVer,
                                   final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findByName(companyId, appName);

        //get compnay
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/rollback");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",appName);
        params.put("model",model);
        params.put("backVer",backVer);
        for (String ip : ipList) {
            params.put("ipList[]",ip);
        }
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }


    @RequestMapping("deployFront")
    @NeedLogin
    public ResponseObject deployFront( @RequestParam String appName, @RequestParam String model,@RequestParam String version,
                                       @RequestParam String type,@RequestParam Boolean forceUpdate,@RequestParam String updateLog) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findByName(companyId, appName);

        //get compnay
        Company company = companyService.findById(companyId);

//        if (StringUtils.isEmpty(version)) {
//            //根据 appName and model 获取最新的
//            version = appVersionService.getDeployVersion(companyId, appName, model);
//        }


        //save appVersion
        AppVersion appVersion = new AppVersion();
        appVersion.setCompanyId(companyId);
        appVersion.setAppName(appName);
        appVersion.setModel(model);
//        appVersion.setAppUrl(appUrl.toString());
        appVersion.setType(type);
        appVersion.setVersion(version);
        appVersion.setForceUpdate(forceUpdate);
        appVersion.setUpdateLog(updateLog);
        appVersion.setStatus(AppVerionStatus.ready.getCode());
        try{
            appVersionService.insert(appVersion);
        }catch (Exception e){
            throw new BizException("发布失败，请确认版本是否重复了."+e.getMessage());
        }
        //end
        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/deployFront");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",appName);
        params.put("model",model);
        params.put("version",version);
        params.put("type",type);
        params.put("appVersionId",appVersion.getId().toString());
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        if(responseRes.getStatus() != HttpStatus.SC_OK){
            return responseObject;
        }
        return responseObject;
    }


}
