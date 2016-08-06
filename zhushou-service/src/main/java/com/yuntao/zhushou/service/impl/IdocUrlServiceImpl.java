package com.yuntao.zhushou.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.dal.mapper.IdocUrlMapper;
import com.yuntao.zhushou.model.domain.IdocParam;
import com.yuntao.zhushou.model.domain.IdocUrl;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.param.IdocDataParam;
import com.yuntao.zhushou.model.query.IdocUrlQuery;
import com.yuntao.zhushou.model.vo.IdocParamVo;
import com.yuntao.zhushou.model.vo.IdocUrlVo;
import com.yuntao.zhushou.model.vo.LogWebVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.service.inter.IdocParamService;
import com.yuntao.zhushou.service.inter.IdocUrlService;
import com.yuntao.zhushou.service.inter.LogService;
import org.apache.commons.collections4.CollectionUtils;
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
    public void save(IdocDataParam idocDataParam,User user) {
        List<IdocParam> paramList = idocDataParam.getParamList();
        IdocUrl idocUrl = BeanUtils.beanCopy(idocDataParam,IdocUrl.class);
        idocUrl.setCreateUserId(user.getId());
        idocUrl.setCreateUserName(user.getNickName());
        this.insert(idocUrl);

        if (CollectionUtils.isNotEmpty(paramList)) {
            for (IdocParam idocParam : paramList) {
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
                idocParam.setParentId(idocUrl.getId());
                idocParamService.insert(idocParam);
            }
        }
    }

}
