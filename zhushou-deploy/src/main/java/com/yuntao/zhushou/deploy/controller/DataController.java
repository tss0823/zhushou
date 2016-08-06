package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.CustomizedPropertyConfigurer;
import com.yuntao.zhushou.common.utils.AppConfigUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.model.domain.Config;
import com.yuntao.zhushou.model.enums.*;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.ConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer;

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
        responseObject.put("paramRuleType",AtParamterRuleType.values());
        responseObject.put("paramDataType",AtParamterDataType.values());
        responseObject.put("moduleType",ModuleType.values());
        return responseObject;
    }

    @RequestMapping("appDataList")
    public ResponseObject appDataList() {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.put("appList",appService.selectAllList());
        List<Config> configList = configService.selectList();
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
}
