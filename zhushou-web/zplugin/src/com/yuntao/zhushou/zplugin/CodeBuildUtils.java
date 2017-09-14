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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shan on 2017/8/29.
 */
public class CodeBuildUtils  {


    private static String zhushouUrl = ZpluginConstant.zhushouUrl;

    private static String loginCookie;

    public static String getLoginCookie(){
        return loginCookie;
    }
    public static String getLoginSid(){
        if (StringUtils.isNotEmpty(loginCookie)) {
            int index = loginCookie.indexOf(";");
            return loginCookie.substring(4,index);
        }
        return "";
    }


    public static ResponseObject login(String accountNo,String pwd) {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "user/login";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("accountNo",accountNo);
        queryMap.put("pwd",pwd);
        requestRes.setParams(queryMap);

//        headers
        Map<String,String> headerMap = new HashMap<>();
        if(StringUtils.isNotEmpty(getLoginCookie())){
            headerMap.put("Cookie",getLoginCookie());
        }
        requestRes.setHeaders(headerMap);

        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
            Map<String, String> headers = responseRes.getHeaders();
            String resCookie = headers.get("Set-Cookie");
            if (StringUtils.isNotEmpty(resCookie)) {
                loginCookie = resCookie;
            }
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

    public static List<Property> propertyList(Long entityId) {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "codeBuild/propertyList";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("entityId", entityId);
        requestRes.setParams(queryMap);
        //headers
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Cookie",getLoginCookie());
        requestRes.setHeaders(headerMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) responseObject.getData();
        List<Property> newDataList = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : dataList) {
            Property property = (Property) BeanUtils.mapToBean(stringObjectMap, Property.class);
            newDataList.add(property);
        }
        return newDataList;
    }


    public static ResponseObject propertySave(Long entityId,List<Property> propertyList) {
        RequestRes requestRes = new RequestRes();
        String url = zhushouUrl + "codeBuild/propertySave";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("id", entityId);
//        List<Property> propertyList = entityParam.getPropertyList();
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
