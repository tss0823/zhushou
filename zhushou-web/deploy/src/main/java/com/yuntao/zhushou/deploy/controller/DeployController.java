package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.HttpParam;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.RequestUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.*;
import com.yuntao.zhushou.model.enums.AppVerionStatus;
import com.yuntao.zhushou.model.query.AppQuery;
import com.yuntao.zhushou.service.inter.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("deploy")
public class DeployController extends BaseController {

    @Autowired
    private DeployService deployService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AppService appService;

    @Autowired
    private AppFrontService appFrontService;


    @Autowired
    private UserService userService;

    @Autowired
    private AppVersionService appVersionService;

    @Autowired
    private HostService hostService;

    @Autowired
    private ProjectService projectService;


    @RequestMapping("branchList")
    @NeedLogin
    public ResponseObject branchList(@RequestParam Long projectId) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();


        //get project
        Project project = projectService.findById(projectId);

        //get company
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/branchList");
        Map<String,String> params = new HashMap<>();
        params.put("codeName",project.getCodeName());
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
//        List<String> dataList = new ArrayList<>();
//        dataList.add("test1");
//        dataList.add("test2");
//        dataList.add("test3");
//        dataList.add("test4");
//        responseObject.setData(dataList);
        return responseObject;
    }


    @RequestMapping("compile")
    @NeedLogin
    public ResponseObject compile(final @RequestParam Long appId,final @RequestParam String branch,final @RequestParam String model) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findById(appId);

        //get project
        Project project = projectService.findById(app.getProjectId());

        //get company
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/compile");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("codeName",project.getCodeName());
        params.put("branch",branch);
        params.put("model",model);
        String compilePropertyJson = app.getCompileProperty();
        try{
            JSONObject jsonObject = new JSONObject(compilePropertyJson);
            Object compileProp = jsonObject.get(model);
            if(compileProp != null){
                params.put("compileProperty",compileProp.toString());
            }
        }catch (Exception e){
            bisLog.error("get compile property json error",e);
        }
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }

    @RequestMapping("compileAndDeploy")
    @NeedLogin
    public ResponseObject compileAndDeploy(final @RequestParam Long appId,final @RequestParam String branch,final @RequestParam String model) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findById(appId);

        //get project
        Project project = projectService.findById(app.getProjectId());

        //get company
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/compileAndDeploy");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("codeName",project.getCodeName());
        params.put("branch",branch);
        params.put("model",model);

        List<HttpParam> paramList = new ArrayList<>();

        //获取该项目下的所有应用列表
        AppQuery appQuery = new AppQuery();
        appQuery.setProjectId(app.getProjectId());
        List<App> appList = appService.selectList(appQuery);
        for (App thisApp : appList) {
            HttpParam httpParam = new HttpParam("appNames[]", thisApp.getName());
            paramList.add(httpParam);
            httpParam = new HttpParam("ports[]", thisApp.getPort().toString());
            paramList.add(httpParam);
            //get ipList
            List<Host> hostList = hostService.selectListByAppAndModel(thisApp.getId(), model);
            List<String> ipList = new ArrayList<>();
            for (Host host : hostList) {
                ipList.add(host.getEth0());
            }
            httpParam = new HttpParam("ipList[]", StringUtils.join(ipList, "|"));
            paramList.add(httpParam);
        }

        String compilePropertyJson = app.getCompileProperty();
        try{
            JSONObject jsonObject = new JSONObject(compilePropertyJson);
            Object compileProp = jsonObject.get(model);
            if(compileProp != null){
                params.put("compileProperty",compileProp.toString());
            }
        }catch (Exception e){
            bisLog.error("get compile property json error",e);
        }
        requestRes.setParams(params);
        requestRes.setParamList(paramList);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }

    @RequestMapping("singleCompileAndDeploy")
    @NeedLogin
    public ResponseObject singleCompileAndDeploy(final @RequestParam Long appId,final @RequestParam String branch,final @RequestParam String model) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findById(appId);

        //get project
        Project project = projectService.findById(app.getProjectId());

        //get company
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/compileAndDeploy");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("codeName",app.getCodeName());
        params.put("branch",branch);
        params.put("model",model);

        List<HttpParam> paramList = new ArrayList<>();

//        List<App> appList = appService.selectByCompanyId(companyId);
//        for (App thisApp : appList) {
            HttpParam httpParam = new HttpParam("appNames[]", app.getName());
            paramList.add(httpParam);
            httpParam = new HttpParam("ports[]", app.getPort().toString());
            paramList.add(httpParam);
            //get ipList
            List<Host> hostList = hostService.selectListByAppAndModel(app.getId(), model);
            List<String> ipList = new ArrayList<>();
            for (Host host : hostList) {
                ipList.add(host.getEth0());
            }
            httpParam = new HttpParam("ipList[]", StringUtils.join(ipList, "|"));
            paramList.add(httpParam);
//        }

        String compilePropertyJson = app.getCompileProperty();
        try{
            JSONObject jsonObject = new JSONObject(compilePropertyJson);
            Object compileProp = jsonObject.get(model);
            if(compileProp != null){
                params.put("compileProperty",compileProp.toString());
            }
        }catch (Exception e){
            bisLog.error("get compile property json error",e);
        }
        requestRes.setParams(params);
        requestRes.setParamList(paramList);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }


    /**
     * 发布
     * 流程控制，1：对应正确是编译模式，不能出现编译test,prod 发布
     * 2：不能跟别的发布流程冲突，比如，编译中，或者重启中
     * @param appId
     * @param ipList
     * @return
     */
    @RequestMapping("deploy")
    @NeedLogin
    public ResponseObject deploy(final @RequestParam Long appId,final @RequestParam String model,
                                 final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findById(appId);

        //get project
        Project project = projectService.findById(app.getProjectId());

        //get compnay
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/deploy");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",app.getName());
        params.put("codeName",project.getCodeName());
        params.put("model",model);
        params.put("ipList",StringUtils.join(ipList,","));
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
     * @param appId
     * @param ipList
     * @return
     */
    @RequestMapping("deployStatic")
    @NeedLogin
    public ResponseObject deployStatic(final @RequestParam Long appId,final @RequestParam String model,
                                       final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findById(appId);

        //get project
        Project project = projectService.findById(app.getProjectId());

        //get company
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/deployStatic");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",app.getName());
        params.put("codeName",project.getCodeName());
        params.put("model",model);
        params.put("ipList",StringUtils.join(ipList,","));
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }

    @RequestMapping("start")
    @NeedLogin
    public ResponseObject start(final @RequestParam Long appId,final @RequestParam String model,final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findById(appId);

        //get project
        Project project = projectService.findById(app.getProjectId());

        //get company
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/start");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",app.getName());
        params.put("model",model);
        params.put("ipList",StringUtils.join(ipList,","));
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }

    @RequestMapping("stop")
    @NeedLogin
    public ResponseObject stop(final @RequestParam Long appId,final @RequestParam String model,final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findById(appId);

        //get project
        Project project = projectService.findById(app.getProjectId());

        //get company
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/stop");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",app.getName());
        params.put("model",model);
        params.put("ipList",StringUtils.join(ipList,","));
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }

    @RequestMapping("restart")
    @NeedLogin
    public ResponseObject restart(final @RequestParam Long appId,final @RequestParam String model,final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findById(appId);

        //get project
        Project project = projectService.findById(app.getProjectId());

        //get company
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/restart");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",app.getName());
        params.put("model",model);
        params.put("ipList",StringUtils.join(ipList,","));
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }

    @RequestMapping("debug")
    @NeedLogin
    public ResponseObject debug(final @RequestParam Long appId,final @RequestParam String model,final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findById(appId);

        //get project
        Project project = projectService.findById(app.getProjectId());

        //get company
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/debug");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",app.getName());
        params.put("model",model);
        params.put("ipList",StringUtils.join(ipList,","));
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }

    @RequestMapping("rollback")
    @NeedLogin
    public ResponseObject rollback(final @RequestParam Long appId,final @RequestParam String model, final @RequestParam String backVer,
                                   final @RequestParam("ipList[]") List<String> ipList) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        App app = appService.findById(appId);

        //get project
        Project project = projectService.findById(app.getProjectId());

        //get company
        Company company = companyService.findById(companyId);

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/rollback");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",app.getName());
        params.put("model",model);
        params.put("backVer",backVer);
        params.put("ipList",StringUtils.join(ipList,","));
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
        return responseObject;
    }


    @RequestMapping("deployFront")
    @NeedLogin
    public ResponseObject deployFront( @RequestParam Long appId, @RequestParam String model,@RequestParam String version,
                                       @RequestParam String type,@RequestParam Boolean forceUpdate,@RequestParam String updateLog) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get app
        AppFront appFront = appFrontService.findById(appId);


        
        //get company
        Company company = companyService.findById(companyId);

//        if (StringUtils.isEmpty(version)) {
//            //根据 appName and model 获取最新的
//            version = appVersionService.getDeployVersion(companyId, appName, model);
//        }


        //save appVersion
        AppVersion appVersion = new AppVersion();
        appVersion.setCompanyId(companyId);
        appVersion.setAppName(appFront.getName());
        appVersion.setModel(model);
//        appVersion.setAppUrl(appUrl.toString());
        appVersion.setType(type);
        appVersion.setVersion(version);
        appVersion.setForceUpdate(forceUpdate);
        appVersion.setUpdateLog(updateLog);
        appVersion.setStatus(AppVerionStatus.ready.getCode());
        try{
            appFrontService.deploy(appFront.getId(),appVersion);
        }catch (Exception e){
            throw new BizException("发布失败，请确认版本是否重复了."+e.getMessage());
        }
        //end
        String branch = appFront.getProdBranch();
        if(model.equals("test")){
            branch = appFront.getTestBranch();
        }
        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/deployFront");
        Map<String,String> params = new HashMap<>();
        params.put("userId",user.getId().toString());
        params.put("nickname",user.getNickName());
        params.put("appName",appFront.getName());
        params.put("br",branch);
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

    @RequestMapping("autoDeploy")
//    @NeedLogin
    public String autoDeploy(HttpServletRequest request) {
        try {
            List<String> paramList = IOUtils.readLines(request.getInputStream());
            if(CollectionUtils.isEmpty(paramList)){
                throw new BizException("param is empty!");
            }
            deployService.autoDeploy(StringUtils.join(paramList,""));
        } catch (IOException e) {
            throw new BizException("autoDeploy error",e);
        }
        return "OK";
    }

    @RequestMapping("addWhiteList")
    @NeedLogin
    public ResponseObject addWhiteList(@RequestParam String ip,HttpServletRequest request) {
        User user = userService.getCurrentUser();
        //call remote method
        Long companyId = user.getCompanyId();

        //get compnay
        Company company = companyService.findById(companyId);

        String clientIp = RequestUtils.getClientIpAddr(request);
        if(StringUtils.isNotEmpty(clientIp)){
            String[] split = clientIp.split(",");
            ip = split[0].trim();
        }

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/addWhiteList");
        Map<String,String> params = new HashMap<>();
        params.put("ip",ip);
        requestRes.setParams(params);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String resData = new String(responseRes.getResult());
        ResponseObject responseObject = JsonUtils.json2Object(resData, ResponseObject.class);
//        List<String> dataList = new ArrayList<>();
//        dataList.add("test1");
//        dataList.add("test2");
//        dataList.add("test3");
//        dataList.add("test4");
//        responseObject.setData(dataList);
        return responseObject;
    }


}
