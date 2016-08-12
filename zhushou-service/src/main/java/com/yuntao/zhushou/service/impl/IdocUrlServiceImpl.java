package com.yuntao.zhushou.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.common.utils.HttpUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.dal.mapper.IdocUrlMapper;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.IdocParam;
import com.yuntao.zhushou.model.domain.IdocUrl;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.param.IdocDataParam;
import com.yuntao.zhushou.model.query.IdocUrlQuery;
import com.yuntao.zhushou.model.vo.IdocParamVo;
import com.yuntao.zhushou.model.vo.IdocUrlVo;
import com.yuntao.zhushou.model.vo.LogWebVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.IdocParamService;
import com.yuntao.zhushou.service.inter.IdocUrlService;
import com.yuntao.zhushou.service.inter.LogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


/**
 * 接口主体服务实现类
 * 
 * @author admin
 *
 * @2016-07-30 20
 */
@Service("idocUrlService")
public class IdocUrlServiceImpl implements IdocUrlService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdocUrlMapper idocUrlMapper;

    @Autowired
    private IdocParamService idocParamService;

    @Autowired
    private LogService logService;

    @Autowired
    private AppService appService;

    @Override
    public List<IdocUrl> selectList(IdocUrlQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return idocUrlMapper.selectList(queryMap);
    }

    @Override
    public Pagination<IdocUrlVo> selectPage(IdocUrlQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = idocUrlMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<IdocUrl> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        queryMap.put("pagination",pagination);
        List<IdocUrl> dataList = idocUrlMapper.selectList(queryMap);
        Pagination<IdocUrlVo> newPageInfo = new Pagination<>(pagination);
        List<IdocUrlVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for(IdocUrl idocUrl : dataList){
            IdocUrlVo idocUrlVo = BeanUtils.beanCopy(idocUrl,IdocUrlVo.class);
            //获取参数
            List<IdocParam> paramList = idocParamService.selectByParentId(idocUrl.getId());
            idocUrlVo.setParamList(paramList);

            //
            String lastTime = DateUtil.getRangeTime(idocUrlVo.getGmtModify());
            idocUrlVo.setLastTime(lastTime);
            newDataList.add(idocUrlVo);
        }
        return newPageInfo;

    }

    @Override
    public IdocUrl findById(Long id) {
        return idocUrlMapper.findById(id);
    }


    @Override
    public int insert(IdocUrl idocUrl) {
        return idocUrlMapper.insert(idocUrl);
    }

    @Override
    public int updateById(IdocUrl idocUrl) {
        return idocUrlMapper.updateById(idocUrl);
    }

    @Override
    public int deleteById(Long id) {
        //delete child first
        idocParamService.deleteByParentId(id);

        return idocUrlMapper.deleteById(id);
    }

    @Override
    public IdocUrlVo bind(String month,String model, String stackId) {
        LogWebVo logWebVo = logService.findMasterByStackId(month, model, stackId);
        IdocUrlVo idocUrlVo = new IdocUrlVo();
        idocUrlVo.setUrl(logWebVo.getUrl());
        String parameters = logWebVo.getParameters();
        Map<String,String> paramMap = JsonUtils.json2Object(parameters, Map.class);
        Set<Map.Entry<String, String>> entrySet = paramMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            IdocParamVo idocParamVo = new IdocParamVo();
            idocParamVo.setCode(entry.getKey());
            idocParamVo.setMemo(entry.getValue());
            idocUrlVo.addParam(idocParamVo);
        }
        String response = logWebVo.getResponse();
        //json 格式化
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object json = mapper.readValue(response, Object.class);
            response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            idocUrlVo.setResultData(response);
        } catch (IOException e) {
        }
        return idocUrlVo;
    }

    @Override
    public IdocUrlVo getIdocUrlVoById(Long id) {
        IdocUrl idocUrl = this.findById(id);
        IdocUrlVo idocUrlVo = BeanUtils.beanCopy(idocUrl, IdocUrlVo.class);

        //获取参数
        List<IdocParam> paramList = idocParamService.selectByParentId(idocUrl.getId());
        idocUrlVo.setParamList(paramList);
        return idocUrlVo;
    }

    @Override
    public void save(IdocDataParam idocDataParam,User user) {
        List<IdocParam> paramList = idocDataParam.getParamList();
        IdocUrl idocUrl = BeanUtils.beanCopy(idocDataParam,IdocUrl.class);
        idocUrl.setCreateUserId(user.getId());
        idocUrl.setCreateUserName(user.getNickName());
        this.insert(idocUrl);

        if (CollectionUtils.isNotEmpty(paramList)) {
            for (IdocParam idocParam : paramList) {
                if (StringUtils.isBlank(idocParam.getCode())) {
                    continue;
                }
                idocParam.setParentId(idocUrl.getId());
                idocParamService.insert(idocParam);
            }
        }

    }

    @Override
    public void update(IdocDataParam idocDataParam, User user) {
        List<IdocParam> paramList = idocDataParam.getParamList();
        IdocUrl idocUrl = BeanUtils.beanCopy(idocDataParam,IdocUrl.class);
        idocUrl.setCreateUserId(user.getId());
        idocUrl.setCreateUserName(user.getNickName());
        this.updateById(idocUrl);

        //先删除
        idocParamService.deleteByParentId(idocUrl.getId());

        //再保存
        if (CollectionUtils.isNotEmpty(paramList)) {
            for (IdocParam idocParam : paramList) {
                if (StringUtils.isBlank(idocParam.getCode())) {
                    continue;
                }
                idocParam.setParentId(idocUrl.getId());
                idocParamService.insert(idocParam);
            }
        }
    }

    @Override
    public void syncNew(String appName) {
        //delete parent
        //get parent data List by appName and type
        IdocUrlQuery query = new IdocUrlQuery();
        query.setAppName(appName);
        query.setType(1);
        List<IdocUrl> idocUrls = this.selectList(query);
        if (CollectionUtils.isNotEmpty(idocUrls)) {
            for (IdocUrl idocUrl : idocUrls) {
                idocUrlMapper.deleteDirectById(idocUrl.getId());
                //delete child dataList
                this.idocParamService.deleteByParentId(idocUrl.getId());
            }
        }
        //end

        App app = appService.findByName(appName);
        String domain = app.getDomain();

        String url = "http://"+ appName+"."+domain+"/data/enumList";
        List<String> resultList = HttpUtils.reqGet(url);
        String result = StringUtils.join(resultList, "");
        Map dataMap = JsonUtils.json2Object(result, Map.class);
        Map<String,List<Map<String,String>>> enumMap = (Map<String, List<Map<String, String>>>) dataMap.get("data");
        Set<Map.Entry<String, List<Map<String, String>>>> entries = enumMap.entrySet();
        for (Map.Entry<String, List<Map<String, String>>> entry : entries) {
            String key = entry.getKey();
            String keyStrs [] = key.split("_");
            String code = keyStrs[0];
            String text = keyStrs[1];
            IdocUrl idocUrl = new IdocUrl();
            idocUrl.setType(1);
            idocUrl.setName(text);
            idocUrl.setUrl(code);
            idocUrl.setAppName(appName);
            idocUrl.setVersion("1.0.0");
            this.insert(idocUrl);

            //items
            List<Map<String, String>> dataList = entry.getValue();
            for (Map<String, String> stringMap : dataList) {

                Object paramCode = stringMap.get("code");
                String paramValue = stringMap.get("description");
                IdocParam idocParam = new IdocParam();
                idocParam.setCode(paramCode.toString());
                idocParam.setName(paramValue);
                idocParam.setParentId(idocUrl.getId());
                idocParam.setStatus(0);
                this.idocParamService.insert(idocParam);
            }
            //end
        }
    }

    @Override
    public void syncUpdate(String appName, String code) {
        //get parent data by appName and type and code
        IdocUrlQuery query = new IdocUrlQuery();
        query.setAppName(appName);
        query.setType(1);
        query.setUrl(code);
        List<IdocUrl> idocUrls = this.selectList(query);
        IdocUrl idocUrl = idocUrls.get(0);
        //delete child data list
        this.idocParamService.deleteByParentId(idocUrl.getId());
        //end

        String url = "http://"+ appName+".api.mynixi.com/data/enumList";
        List<String> resultList = HttpUtils.reqGet(url);
        String result = StringUtils.join(resultList, "");
        Map<String,List<Map<String,String>>> enumMap = JsonUtils.json2Object(result, Map.class);
        Set<Map.Entry<String, List<Map<String, String>>>> entries = enumMap.entrySet();
        for (Map.Entry<String, List<Map<String, String>>> entry : entries) {
            String key = entry.getKey();
            String keyStrs[] = key.split("_");
            String parentCode = keyStrs[0];
            String text = keyStrs[1];
            if (!parentCode.equals(idocUrl.getUrl())) {
                continue;
            }
            idocUrl.setName(text);
            idocUrl.setUrl(code);
            idocUrl.setAppName(appName);
            idocUrl.setVersion("1.0.0");
            this.updateById(idocUrl);

            //items
            List<Map<String, String>> dataList = entry.getValue();
            for (Map<String, String> stringMap : dataList) {
                Set<Map.Entry<String, String>> childSet = stringMap.entrySet();
                for (Map.Entry<String, String> childEntry : childSet) {
                    String paramCode = childEntry.getKey();
                    String paramValue = childEntry.getValue();
                    IdocParam idocParam = new IdocParam();
                    idocParam.setCode(paramCode);
                    idocParam.setName(paramValue);
                    idocParam.setParentId(idocUrl.getId());
                    idocParam.setStatus(0);
                    this.idocParamService.insert(idocParam);
                }
            }
            //end
        }
    }

}
