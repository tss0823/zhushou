package com.yuntao.zhushou.deploy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.utils.UrlUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.ShowRes;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.param.DataMap;
import com.yuntao.zhushou.model.param.IdocDataParam;
import com.yuntao.zhushou.model.param.ReqDataParam;
import com.yuntao.zhushou.model.query.ShowResQuery;
import com.yuntao.zhushou.model.vo.ShowResVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.ShowResService;
import com.yuntao.zhushou.service.inter.ShowResService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("showRes")
public class ShowResController extends BaseController {


    @Autowired
    private ShowResService showResService;

    @Autowired
    private AppService appService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(ShowResQuery query) {
        Pagination<ShowResVo> pagination = showResService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

//    @RequestMapping("getDataById")
//    @NeedLogin
//    public ResponseObject getDataById(@RequestParam Long id) {
//        ShowRes showRes = showResService.findById(id);
//        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
//        responseObject.setData(showRes);
//        return responseObject;
//    }

    @RequestMapping("save")
    @NeedLogin
    public ResponseObject save(ShowRes showRes) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
//        User user = userService.getCurrentUser();
        showResService.insert(showRes);
        return responseObject;
    }

    @RequestMapping("update")
    @NeedLogin
    public ResponseObject update(ShowRes showRes) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        showResService.updateById(showRes);
        return responseObject;
    }

    @RequestMapping("deleteById")
    @NeedLogin
    public ResponseObject deleteById(Long id) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        showResService.deleteById(id);
        return responseObject;
    }




}
