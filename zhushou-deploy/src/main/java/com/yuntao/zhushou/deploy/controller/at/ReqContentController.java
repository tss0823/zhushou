package com.yuntao.zhushou.deploy.controller.at;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.ReqContent;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.param.DataMap;
import com.yuntao.zhushou.model.param.ReqDataParam;
import com.yuntao.zhushou.model.query.ReqContentQuery;
import com.yuntao.zhushou.model.vo.ReqContentVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.ReqContentService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private AppService appService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(ReqContentQuery query) {
        Pagination<ReqContentVo> pagination = reqContentService.selectPage(query);
        if(CollectionUtils.isNotEmpty(pagination.getDataList())){
            for (ReqContentVo reqContentVo : pagination.getDataList()) {
                String resData = reqContentVo.getResData();
                //json 格式化
                ObjectMapper mapper = new ObjectMapper();
                try {
                    Object json = mapper.readValue(resData, Object.class);
                    resData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                } catch (IOException e) {
                }
                reqContentVo.setResData(resData);
                //end

            }
        }
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

//    @RequestMapping("getDataById")
//    @NeedLogin
//    public ResponseObject getDataById(@RequestParam Long id) {
//        ReqContent reqContent = reqContentService.findById(id);
//        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
//        responseObject.setData(reqContent);
//        return responseObject;
//    }

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
            for (DataMap dataMap : dataList) {
                paramMap.put(dataMap.getKey(),dataMap.getValue());
            }
        }
        requestRes.setParams(paramMap);
        //url 处理
        String appName = param.getAppName();
        App app = appService.findByName(appName);
        String domain = app.getDomain();
        String model = param.getModel();  //model 之后处理 TODO
        String urlPrefix = appName;
        if(urlPrefix.equals("member")){  //特殊处理
            urlPrefix = "user";
        }
        String url = "http://"+urlPrefix+"."+domain+param.getUrl();
        requestRes.setUrl(url);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);

        //store to db
        User user = userService.getCurrentUser();
        ReqContent reqContent = new ReqContent();
        reqContent.setUserId(user.getId());
        reqContent.setUserName(user.getNickName());
        reqContent.setUrl(param.getUrl());
        reqContent.setHttpStatus(responseRes.getStatus());
        reqContent.setName(param.getUrl());
        reqContent.setAppName(appName);
        reqContent.setModel(model);
        reqContent.setReqHeader(JsonUtils.object2Json(requestRes.getHeaders()));
        reqContent.setReqData(JsonUtils.object2Json(requestRes.getParams()));
        reqContent.setResHeader(JsonUtils.object2Json(responseRes.getHeaders()));
        reqContent.setResData(new String(responseRes.getResult()));
        reqContent.setStatus(1);
        reqContentService.insert(reqContent);
        //end



        responseObject.put("resHeaderList",responseRes.getHeaders());
        String resData = new String(responseRes.getResult());
        //json 格式化
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object json = mapper.readValue(resData, Object.class);
            resData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (IOException e) {
        }
        //end
        responseObject.put("resData",resData);
        responseObject.put("status",responseRes.getStatus());
        return responseObject;
    }



}
