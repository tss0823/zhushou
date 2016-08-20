package com.yuntao.zhushou.deploy.controller.at;

import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.ReqContent;
import com.yuntao.zhushou.model.param.DataMap;
import com.yuntao.zhushou.model.param.ReqDataParam;
import com.yuntao.zhushou.model.query.ReqContentQuery;
import com.yuntao.zhushou.model.vo.ReqContentVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.ReqContentService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("reqContent")
public class ReqContentController extends BaseController {


    @Autowired
    private ReqContentService reqContentService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(ReqContentQuery query) {
        Pagination<ReqContentVo> pagination = reqContentService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("save")
    @NeedLogin
    public ResponseObject save(ReqContentQuery query,@RequestParam(value="myFile" ,required = false) MultipartFile [] myFiles) {
//        Pagination<ReqContentVo> pagination = reqContentService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
//        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("execute")
    @NeedLogin
    public ResponseObject execute(@ModelAttribute ReqDataParam param) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        RequestRes requestRes = new RequestRes();
        requestRes.setUrl(param.getUrl());
        List<DataMap> headerList = param.getHeaderList();
        Map<String,String> headerMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(headerList)) {
            for (DataMap dataMap : headerList) {
                headerMap.put(dataMap.getKey(),dataMap.getValue());
            }
        }
        requestRes.setHeaders(headerMap);
        List<DataMap> dataList = param.getDataList();
        Map<String,String> paramMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (DataMap dataMap : headerList) {
                paramMap.put(dataMap.getKey(),dataMap.getValue());
            }
        }
        requestRes.setParams(paramMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        responseObject.put("headers",responseRes.getHeaders());
        responseObject.put("result",new String(responseRes.getResult()));
        responseObject.put("status",responseRes.getStatus());
        return responseObject;
    }



}
