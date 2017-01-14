package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.AppConfigUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.model.domain.Config;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.enums.*;
import com.yuntao.zhushou.model.query.IdocUrlQuery;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.ConfigService;
import com.yuntao.zhushou.service.inter.IdocUrlService;
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
        responseObject.put("yesNoType",YesNoType.values());
        responseObject.put("logStatus",LogStatus.values());
        responseObject.put("cacheType",CacheType.values());
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
        responseObject.put("docList",idocUrlService.selectList(idocUrlQuery));
        return responseObject;
    }
}
