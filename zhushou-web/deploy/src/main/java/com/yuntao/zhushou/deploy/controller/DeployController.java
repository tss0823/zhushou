package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.CompanyService;
import com.yuntao.zhushou.service.inter.UserService;
import com.yuntao.zhushou.service.support.YTWebSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("deploy")
public class DeployController extends BaseController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AppService appService;


    @Autowired
    private UserService userService;


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



}
