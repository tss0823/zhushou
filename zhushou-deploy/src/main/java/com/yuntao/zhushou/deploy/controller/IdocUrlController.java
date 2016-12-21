package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.HttpUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.IdocParam;
import com.yuntao.zhushou.model.domain.IdocUrl;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.param.IdocDataParam;
import com.yuntao.zhushou.model.query.IdocUrlQuery;
import com.yuntao.zhushou.model.vo.IdocUrlVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.IdocUrlService;
import com.yuntao.zhushou.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("idocUrl")
public class IdocUrlController extends BaseController {


    @Autowired
    private IdocUrlService idocUrlService;

    @Autowired
    private UserService userService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(IdocUrlQuery query) {
        if (query.getType() == null) {
            query.setType(0);
        }
        Pagination<IdocUrlVo> pagination = idocUrlService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("bind")
    @NeedLogin
    public ResponseObject bind(@RequestParam String month,@RequestParam String model,@RequestParam String stackId) {
        IdocUrlVo idocUrlVo = idocUrlService.bind(month, model, stackId);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(idocUrlVo);
        return responseObject;
    }

    @RequestMapping("submitDoc")
    @NeedLogin
    public ResponseObject submitDoc(@RequestParam String jsonDoc) {
        User user = userService.getCurrentUser();
        idocUrlService.submitDoc(jsonDoc,user);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData("ok");
        return responseObject;
    }

    @RequestMapping("getIdocUrlVoById")
    @NeedLogin
    public ResponseObject getIdocUrlVoById(@RequestParam Long id) {
        IdocUrlVo idocUrlVo = idocUrlService.getIdocUrlVoById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(idocUrlVo);
        return responseObject;
    }

    @RequestMapping("save")
    @NeedLogin
    public ResponseObject save(IdocDataParam idocDataParam) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.getCurrentUser();
        idocDataParam.setType(0);
        idocUrlService.save(idocDataParam,user);
        return responseObject;
    }

    @RequestMapping("update")
    @NeedLogin
    public ResponseObject update(IdocDataParam idocDataParam) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.getCurrentUser();
        idocDataParam.setType(0);
        idocUrlService.update(idocDataParam,user);
        return responseObject;
    }

    @RequestMapping("deleteById")
    @NeedLogin
    public ResponseObject deleteById(Long id) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        idocUrlService.deleteById(id);
        return responseObject;
    }

    @RequestMapping("syncNew")
    @NeedLogin
    public ResponseObject syncNew(String appName) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        idocUrlService.syncNew(appName);
        return responseObject;
    }

    @RequestMapping("syncUpdate")
    @NeedLogin
    public ResponseObject syncUpdate(@RequestParam String appName,@RequestParam Long id) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        idocUrlService.syncUpdate(appName,id);
        return responseObject;
    }

    @RequestMapping("submitEnum")
    @NeedLogin
    public ResponseObject submitEnum(@RequestParam String appName,@RequestParam String jsonEnum) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        idocUrlService.submitEnum(appName,jsonEnum);
        return responseObject;
    }



}
