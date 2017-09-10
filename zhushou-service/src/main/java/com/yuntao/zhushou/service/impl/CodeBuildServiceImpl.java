package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.HttpParam;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.codeBuild.DbConfigure;
import com.yuntao.zhushou.model.domain.codeBuild.Entity;
import com.yuntao.zhushou.model.domain.codeBuild.Property;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import com.yuntao.zhushou.model.query.codeBuild.EntityQuery;
import com.yuntao.zhushou.model.query.codeBuild.PropertyQuery;
import com.yuntao.zhushou.model.vo.codeBuild.ResultObj;
import com.yuntao.zhushou.service.inter.CodeBuildService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shan on 2017/8/29.
 */
@Service
public class CodeBuildServiceImpl extends AbstService implements CodeBuildService {

    @Value("${codeBuild.url}")
    private String codeBuildUrl;

    @Override
    public List<Entity> selectList(EntityQuery query) {
        return null;
    }

    @Override
    public Pagination<Entity> selectPage(EntityQuery query) {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/entityList.do";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = BeanUtils.beanToMapNotNull(query);
        requestRes.setParams(queryMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        return JsonUtils.json2Object(bodyText, Pagination.class);
    }

    @Override
    public int entitySave(Entity entity) {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/entitySave.do";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = BeanUtils.beanToMapNotNull(entity);
        requestRes.setParams(queryMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
            if (resultObj.getResult() == 0) {
                throw new BizException(resultObj.getData().toString());
            } else {
                return 1;
            }
        }else{
            throw new BizException(bodyText);
        }
    }

    @Override
    public int entityUpdate(Entity entity) {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/entityUpdate.do";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = BeanUtils.beanToMapNotNull(entity);
        requestRes.setParams(queryMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
        if (resultObj.getResult() == 0) {
            throw new BizException(resultObj.getData().toString());
        } else {
            return 1;
        }
    }

    @Override
    public Entity entityDetail(Long id) {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/entityDetail.do";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("id", id);
        requestRes.setParams(queryMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
        if (resultObj.getResult() == 0) {
            throw new BizException(resultObj.getData().toString());
        } else {
            Map<String, Object> dataMap = (Map<String, Object>) resultObj.getData();
            Entity entity = new Entity();
            BeanUtils.mapToBean(dataMap, entity);
            return entity;
        }
    }

    @Override
    public int entityDelete(Long id) {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/entityDelete.do";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("id", id);
        requestRes.setParams(queryMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
        if (resultObj.getResult() == 0) {
            throw new BizException(resultObj.getData().toString());
        } else {
            return 1;
        }
    }

    @Override
    public int entityCopy(Long id) {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/entityCopy.do";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("id", id);
        requestRes.setParams(queryMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
        if (resultObj.getResult() == 0) {
            throw new BizException(resultObj.getData().toString());
        } else {
            return 1;
        }
    }

    @Override
    public String buildSql(Long appId, String ids) {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/buildSql.do";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("appId", appId);
        queryMap.put("ids", ids);
        requestRes.setParams(queryMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
            if (resultObj.getResult() == 0) {
                throw new BizException(resultObj.getData().toString());
            } else {
                return resultObj.getData().toString();
            }
        }else{
            throw new BizException(bodyText);

        }
    }

    @Override
    public String buildApp(Long appId, String ids) {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/buildApp.do";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("appId", appId);
        queryMap.put("ids", ids);
        requestRes.setParams(queryMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
            if (resultObj.getResult() == 0) {
                throw new BizException(resultObj.getData().toString());
            } else {
                return resultObj.getData().toString();
            }
        }else {
            throw new BizException(bodyText);
        }
    }

    @Override
    public List<Property> propertyList(PropertyQuery query) {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/propertyList.do";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = BeanUtils.beanToMapNotNull(query);
        requestRes.setParams(queryMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultObj.getData();
        List<Property> newDataList = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : dataList) {
            Property property = new Property();
            BeanUtils.mapToBean(stringObjectMap, property);
            newDataList.add(property);
        }
        return newDataList;
    }

    @Override
    public Map<String, String> dataTypeEnums() {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/dataTypeEnums.do";
        requestRes.setUrl(url);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        return JsonUtils.json2Object(bodyText, Map.class);
    }

    @Override
    public int propertySave(EntityParam entityParam) {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/propertySave.do";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("entityId", entityParam.getId());
        requestRes.setParams(queryMap);
        List<Property> propertyList = entityParam.getPropertyList();
        List<HttpParam> paramList = new ArrayList<>();
        for (Property property : propertyList) {
            HttpParam httpParam = new HttpParam("enName", property.getEnName());
            paramList.add(httpParam);

            httpParam = new HttpParam("cnName", property.getCnName());
            paramList.add(httpParam);

            httpParam = new HttpParam("dataType", property.getDataType());
            paramList.add(httpParam);

            httpParam = new HttpParam("length", property.getLength());
            paramList.add(httpParam);


            httpParam = new HttpParam("defaultValue", property.getDefaultValue());
            paramList.add(httpParam);

            httpParam = new HttpParam("isNull", property.getIsNull());
            paramList.add(httpParam);

            httpParam = new HttpParam("primaryKey", property.getPrimaryKey());
            paramList.add(httpParam);
        }
        requestRes.setParamList(paramList);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
            if (resultObj.getResult() == 0) {
                throw new BizException(resultObj.getData().toString());
            } else {
                return 1;
            }
        }else{
            throw new BizException(bodyText);
        }
    }

    @Override
    public DbConfigure getDbConfigure(Long appId) {
        RequestRes requestRes = new RequestRes();
        String url = codeBuildUrl + "api/getDbConfigure.do";
        requestRes.setUrl(url);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("appId", appId);
        requestRes.setParams(queryMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        ResultObj resultObj = JsonUtils.json2Object(bodyText, ResultObj.class);
        if (resultObj.getResult() == 0) {
            throw new BizException(resultObj.getData().toString());
        } else {
            Map<String, Object> dataMap = (Map<String, Object>) resultObj.getData();
            DbConfigure dbConfigure = new DbConfigure();
            BeanUtils.mapToBean(dataMap, dbConfigure);
            return dbConfigure;
        }
    }
}
