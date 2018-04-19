package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.DeployLog;
import com.yuntao.zhushou.model.domain.Host;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.DeployLogQuery;
import com.yuntao.zhushou.model.query.HostQuery;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.DeployLogService;
import com.yuntao.zhushou.service.inter.HostService;
import com.yuntao.zhushou.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("host")
public class HostController extends BaseController {

    @Autowired
    private HostService hostService;

    @Autowired
    private AppService appService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeployLogService deployLogService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(HostQuery query) {
        User user = userService.getCurrentUser();
        query.setCompanyId(user.getCompanyId());
        Pagination<Host> pagination = hostService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("getListByAppAndModel")
    @NeedLogin
    public ResponseObject getHostListByAppAndModel(@RequestParam Long appId, String model) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<Host> dataList = hostService.selectListByAppAndModel(appId,model);
        App app = appService.findById(appId);
        responseObject.put("app",app);
        responseObject.put("dataList",dataList);
        return responseObject;
    }

    @RequestMapping("getLastBackVer")
    @NeedLogin
    public ResponseObject getLastBackVer(@RequestParam String appName,String ip) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        DeployLogQuery deployLogQuery = new DeployLogQuery();
        deployLogQuery.setTopNum(5);
        deployLogQuery.setAppName(appName);
        deployLogQuery.setBackVerLike(ip);
        List<DeployLog> deployLogList = deployLogService.selectList(deployLogQuery);
        responseObject.setData(deployLogList);
        return responseObject;
    }

    @RequestMapping("detail")
    @NeedLogin
    public ResponseObject detail(@RequestParam Long id) {
        Host host = hostService.findById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(host);
        return responseObject;
    }

    @RequestMapping("save")
    @NeedLogin
    public ResponseObject save(Host host) {
        User user = userService.getCurrentUser();
        host.setCompanyId(user.getCompanyId());
        int result = hostService.insert(host);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("update")
    @NeedLogin
    public ResponseObject update(Host host) {
        Assert.notNull(host.getId(),"id 不能为空");
        int result = hostService.updateById(host);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("deleteById")
    @NeedLogin
    public ResponseObject deleteById(@RequestParam Long id) {
        int result = hostService.deleteById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }


}
