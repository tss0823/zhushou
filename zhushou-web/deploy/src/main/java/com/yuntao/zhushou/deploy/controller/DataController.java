package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.AppConfigUtils;
import com.yuntao.zhushou.common.utils.CollectUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.*;
import com.yuntao.zhushou.model.enums.*;
import com.yuntao.zhushou.model.query.*;
import com.yuntao.zhushou.model.vo.IdocUrlFrontVo;
import com.yuntao.zhushou.service.inter.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("data")
public class DataController extends  BaseController {

    @Autowired
    private AppService appService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private IdocUrlService idocUrlService;

    @Autowired
    private AtTemplateService atTemplateService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private HostService hostService;

    @RequestMapping("appConfigData")
    public ResponseObject appConfigData() {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        return responseObject;
    }

    @RequestMapping("enums")
    public ResponseObject dataList() {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.put("userStatus",UserStatus.values());
        responseObject.put("userType", UserType.values());
        responseObject.put("logLevel", LogLevel.values());
        responseObject.put("logQueryType", LogQueryType.values());
        responseObject.put("logQueryTextCat", LogQueryTextCat.values());
        responseObject.put("logModel", LogModel.values());
        responseObject.put("collectType", AtActiveCollectType.values());
//        responseObject.put("paramRuleType", AtParameterDataType.values());
        responseObject.put("paramDataType", AtParameterDataType.values());
        responseObject.put("moduleType",ModuleType.values());
        responseObject.put("yesNoType",YesNoType.values());
        responseObject.put("yesNoIntType",YesNoIntType.values());
        responseObject.put("logStatus",LogStatus.values());
        responseObject.put("cacheType",CacheType.values());
        responseObject.put("javaDataType",JavaDataType.values());
        responseObject.put("templateType",TemplateType.values());
        return responseObject;
    }

    @RequestMapping("appDataList")
    public ResponseObject appDataList() {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<Config> configList = configService.selectPubList();
        if (CollectionUtils.isNotEmpty(configList)) {
            for (Config config : configList) {
                responseObject.put(config.getName(),config.getValue());
            }
        }
        //
        String serverCheck = AppConfigUtils.getValue("server.check");
        responseObject.put("serverCheck",Boolean.valueOf(serverCheck));
        return responseObject;
    }

    @RequestMapping("appUserDataList")
    public ResponseObject appUserDataList(@RequestParam  Long userId) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.findById(userId);

        responseObject.put("appList",appService.selectByCompanyId(user.getCompanyId()));

        IdocUrlQuery idocUrlQuery = new IdocUrlQuery();
        idocUrlQuery.setType(0);
        idocUrlQuery.setCompanyId(user.getCompanyId());
        List<IdocUrl> idocUrls = idocUrlService.selectList(idocUrlQuery);
        List<Object> docList = CollectUtils.transList(idocUrls, IdocUrlFrontVo.class);
        responseObject.put("docList",docList);
        return responseObject;
    }

    @RequestMapping("atTemplateList")
    @NeedLogin
    public ResponseObject atTemplateList() {
        ResponseObject responseObject = new ResponseObject();
        User user = userService.getCurrentUser();
        AtTemplateQuery query = new AtTemplateQuery();
        query.setCompanyId(user.getCompanyId());
        List<AtTemplate> templateList = atTemplateService.selectList(query);
        responseObject.setData(templateList);
        return responseObject;
    }

    @RequestMapping("projectList")
    @NeedLogin
    public ResponseObject projectList() {
        ResponseObject responseObject = new ResponseObject();
        User user = userService.getCurrentUser();
        ProjectQuery query = new ProjectQuery();
        query.setCompanyId(user.getCompanyId());
        List<Project> projectList = projectService.selectList(query);
        responseObject.setData(projectList);
        return responseObject;
    }
    @RequestMapping("hostList")
    @NeedLogin
    public ResponseObject hostList() {
        ResponseObject responseObject = new ResponseObject();
        User user = userService.getCurrentUser();
        HostQuery query = new HostQuery();
        query.setCompanyId(user.getCompanyId());
        List<Host> dataList = hostService.selectList(query);
        responseObject.setData(dataList);
        return responseObject;
    }

    @RequestMapping("templateList")
    @NeedLogin
    public ResponseObject templateList() {
        ResponseObject responseObject = new ResponseObject();
        User user = userService.getCurrentUser();
        TemplateQuery query = new TemplateQuery();
        query.setCompanyId(user.getCompanyId());
        List<Template> dataList = templateService.selectList(query);
        responseObject.setData(dataList);
        return responseObject;
    }
}
