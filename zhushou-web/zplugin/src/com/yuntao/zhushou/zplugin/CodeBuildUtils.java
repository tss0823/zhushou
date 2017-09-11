package com.yuntao.zhushou.zplugin;

import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.model.domain.codeBuild.Entity;
import com.yuntao.zhushou.model.domain.codeBuild.Property;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shan on 2017/8/29.
 */
public class CodeBuildUtils  {

    private static String zhushouUrl = "http://zhushou.doublefit.cn/";


    private static String loginCookie;

    public static String getLoginCookie(){
        return loginCookie;
    }

//    public static Pagination<Entity> selectPage(EntityQuery query) {
//        RequestRes requestRes = new RequestRes();
//        String url = zhushouUrl + "api/entityList.do";
//        requestRes.setUrl(url);
//        Map<String, Object> queryMap = BeanUtils.beanToMapNotNull(query);
//        requestRes.setParams(queryMap);
//        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
//        String bodyText = responseRes.getBodyText();
//        return JsonUtils.json2Object(bodyText, Pagination.class);
//    }

    public static ResponseObject login(String accountNo,String pwd) {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "user/login";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("accountNo",accountNo);
        queryMap.put("pwd",pwd);
        requestRes.setParams(queryMap);

        //headers
//        Map<String,String> headerMap = new HashMap<>();
//        requestRes.setHeaders(headerMap);

        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
            Map<String, String> headers = responseRes.getHeaders();
            loginCookie = headers.get("Set-Cookie");
            return responseObject;
        }else{
            throw new BizException(bodyText);
        }
    }

    public static ResponseObject entitySave(Entity entity) {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "codeBuild/entitySave";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = BeanUtils.beanToMapNotNull(entity);
        requestRes.setParams(queryMap);
        //headers
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Cookie",getLoginCookie());
        requestRes.setHeaders(headerMap);

        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
            return responseObject;
        }else{
            throw new BizException(bodyText);
        }
    }

    public static ResponseObject entityUpdate(Entity entity) {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "codeBuild/entityUpdate";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = BeanUtils.beanToMapNotNull(entity);
        requestRes.setParams(queryMap);
        //headers
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Cookie",getLoginCookie());
        requestRes.setHeaders(headerMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
            return responseObject;
        }else{
            throw new BizException(bodyText);
        }
    }

    public static ResponseObject getEntityByEnName(String enName) {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "codeBuild/getEntityByEnName";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("enName", enName);
        requestRes.setParams(queryMap);
        //headers
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Cookie",getLoginCookie());
        requestRes.setHeaders(headerMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
            return responseObject;
        }else{
            throw new BizException(bodyText);
        }
    }

//    public static Entity entityDetail(Long id) {
//        RequestRes requestRes = new RequestRes();
//        String url = zhushouUrl + "api/entityDetail.do";
//        requestRes.setUrl(url);
//        Map<String, Object> queryMap = new HashMap();
//        queryMap.put("id", id);
//        requestRes.setParams(queryMap);
//        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
//        String bodyText = responseRes.getBodyText();
//        ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
//        if (resultObj.getResult() == 0) {
//            throw new BizException(resultObj.getData().toString());
//        } else {
//            Map<String, Object> dataMap = (Map<String, Object>) resultObj.getData();
//            Entity entity = new Entity();
//            BeanUtils.mapToBean(dataMap, entity);
//            return entity;
//        }
//    }

    public static ResponseObject entityDelete(Long id) {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "codeBuild/entityDelete";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("id", id);
        requestRes.setParams(queryMap);
        //headers
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Cookie",getLoginCookie());
        requestRes.setHeaders(headerMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
            return responseObject;
        }else{
            throw new BizException(bodyText);
        }
    }

//    public static int entityCopy(Long id) {
//        RequestRes requestRes = new RequestRes();
//        String url = zhushouUrl + "api/entityCopy.do";
//        requestRes.setUrl(url);
//        Map<String, Object> queryMap = new HashMap();
//        queryMap.put("id", id);
//        requestRes.setParams(queryMap);
//        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
//        String bodyText = responseRes.getBodyText();
//        ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
//        if (resultObj.getResult() == 0) {
//            throw new BizException(resultObj.getData().toString());
//        } else {
//            return 1;
//        }
//    }

    public static ResponseObject buildSql(String ids) {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "codeBuild/buildSql";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("ids", ids);
        requestRes.setParams(queryMap);
        //headers
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Cookie",getLoginCookie());
        requestRes.setHeaders(headerMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
            return responseObject;
        }else{
            throw new BizException(bodyText);
        }
    }

    public static ResponseObject getDbConfigure() {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "codeBuild/getDbConfigure";
        requestRes.setUrl(url);
        //headers
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Cookie",getLoginCookie());
        requestRes.setHeaders(headerMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
            return responseObject;
        }else{
            throw new BizException(bodyText);
        }
    }

    public static ResponseObject buildApp(String ids) {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "codeBuild/buildApp";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("ids", ids);
        requestRes.setParams(queryMap);
        //headers
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Cookie",getLoginCookie());
        requestRes.setHeaders(headerMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
            return responseObject;
        }else{
            throw new BizException(bodyText);
        }
    }

//    public static List<Property> propertyList(PropertyQuery query) {
//        RequestRes requestRes = new RequestRes();
//        String url = zhushouUrl + "codeBuild/propertyList.do";
//        requestRes.setUrl(url);
//        Map<String, Object> queryMap = BeanUtils.beanToMapNotNull(query);
//        requestRes.setParams(queryMap);
//        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
//        String bodyText = responseRes.getBodyText();
//        ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
//        List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultObj.getData();
//        List<Property> newDataList = new ArrayList<>();
//        for (Map<String, Object> stringObjectMap : dataList) {
//            Property property = new Property();
//            BeanUtils.mapToBean(stringObjectMap, property);
//            newDataList.add(property);
//        }
//        return newDataList;
//    }


    public static ResponseObject propertySave(EntityParam entityParam) {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "codeBuild/propertySave";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("id", entityParam.getId());
        List<Property> propertyList = entityParam.getPropertyList();
        int index = 0;
        for (Property property : propertyList) {
            queryMap.put("propertyList["+index+"].enName",property.getEnName());
            queryMap.put("propertyList["+index+"].cnName",property.getCnName());
            queryMap.put("propertyList["+index+"].dataType",property.getDataType());
            queryMap.put("propertyList["+index+"].length",property.getLength());
            queryMap.put("propertyList["+index+"].defaultValue",property.getDefaultValue());
            queryMap.put("propertyList["+index+"].isNull",property.getIsNull());
            queryMap.put("propertyList["+index+"].primaryKey",property.getPrimaryKey());
            index++;
        }


        requestRes.setParams(queryMap);
        //headers
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Cookie",getLoginCookie());
        requestRes.setHeaders(headerMap);
//        List<HttpParam> paramList = new ArrayList<>();
//        for (Property property : propertyList) {
//            HttpParam httpParam = new HttpParam("enName", property.getEnName());
//            paramList.add(httpParam);
//
//            httpParam = new HttpParam("cnName", property.getCnName());
//            paramList.add(httpParam);
//
//            httpParam = new HttpParam("dataType", property.getDataType());
//            paramList.add(httpParam);
//
//            httpParam = new HttpParam("length", property.getLength());
//            paramList.add(httpParam);
//
//
//            httpParam = new HttpParam("defaultValue", property.getDefaultValue());
//            paramList.add(httpParam);
//
//            httpParam = new HttpParam("isNull", property.getIsNull());
//            paramList.add(httpParam);
//
//            httpParam = new HttpParam("primaryKey", property.getPrimaryKey());
//            paramList.add(httpParam);
//        }
//        requestRes.setParamList(paramList);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
            return responseObject;
        }else{
            throw new BizException(bodyText);
        }
    }
}
