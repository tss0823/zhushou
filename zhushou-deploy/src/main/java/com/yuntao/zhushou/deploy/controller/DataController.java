package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.model.enums.*;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("data")
public class DataController extends  BaseController {

    @Autowired
    private AppService appService;

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
        return responseObject;
    }
}
