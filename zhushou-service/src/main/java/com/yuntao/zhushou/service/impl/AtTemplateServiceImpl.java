package com.yuntao.zhushou.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.CollectUtils;
import com.yuntao.zhushou.dal.mapper.AtTemplateMapper;
import com.yuntao.zhushou.model.domain.AtActive;
import com.yuntao.zhushou.model.domain.AtParameter;
import com.yuntao.zhushou.model.domain.AtTemplate;
import com.yuntao.zhushou.model.enums.AtParamterRuleType;
import com.yuntao.zhushou.model.query.AtActiveQuery;
import com.yuntao.zhushou.model.query.AtParameterQuery;
import com.yuntao.zhushou.model.query.AtTemplateQuery;
import com.yuntao.zhushou.model.vo.AtActiveVo;
import com.yuntao.zhushou.model.vo.AtParameterVo;
import com.yuntao.zhushou.model.vo.AtTemplateVo;
import com.yuntao.zhushou.model.vo.LogWebVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.service.inter.AtActiveService;
import com.yuntao.zhushou.service.inter.AtParameterService;
import com.yuntao.zhushou.service.inter.AtTemplateService;
import com.yuntao.zhushou.service.inter.LogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 测试模板服务实现类
 *
 * @author admin
 * @2016-07-21 15
 */
@Service("atTemplateService")
public class AtTemplateServiceImpl implements AtTemplateService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AtTemplateMapper atTemplateMapper;

    @Autowired
    private LogService logService;

    @Autowired
    private AtActiveService atActiveService;

    @Autowired
    private AtParameterService atParameterService;


    @Override
    public List<AtTemplate> selectList(AtTemplateQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return atTemplateMapper.selectList(queryMap);
    }

    @Override
    public Pagination<AtTemplateVo> selectPage(AtTemplateQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = atTemplateMapper.selectListCount(queryMap);
        Pagination<AtTemplate> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        Pagination<AtTemplateVo> newPageInfo = new Pagination<>(pagination);
        if (totalCount == 0) {
            return newPageInfo;
        }
        queryMap.put("pagination", pagination);
        List<AtTemplate> dataList = atTemplateMapper.selectList(queryMap);
        List<AtTemplateVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for (AtTemplate atTemplate : dataList) {
            AtTemplateVo atTemplateVo = BeanUtils.beanCopy(atTemplate, AtTemplateVo.class);
            newDataList.add(atTemplateVo);
        }
        return newPageInfo;

    }

    @Override
    public AtTemplate findById(Long id) {
        return atTemplateMapper.findById(id);
    }


    @Override
    public int insert(AtTemplate atTemplate) {
        return atTemplateMapper.insert(atTemplate);
    }

    @Override
    public int updateById(AtTemplate atTemplate) {
        return atTemplateMapper.updateById(atTemplate);
    }

    @Override
    public int deleteById(Long id) {
        return atTemplateMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void save(AtTemplateVo templateVo, List<String> logIds) {
        String model = templateVo.getModel();
        String month = templateVo.getMonth();
        AtTemplate template = BeanUtils.beanCopy(templateVo, AtTemplate.class);
        this.insert(template);

        //处理活动模板
        for (String logId : logIds) {
            LogWebVo logWebVo = logService.findMasterByStackId(month, model, logId);
            AtActive active = new AtActive();
//            active.setName(logWebVo.getUrl());
            active.setUrl(logWebVo.getUrl());

            String reqHeaders = logWebVo.getReqHeaders();
            JSONObject headerJsonObj = JSON.parseObject(reqHeaders);

            String conentType = headerJsonObj.getString("Content-Type");
            active.setReqContentType(conentType);

            active.setHeaderRow(logWebVo.getReqHeaders());
            active.setMethod("POST");
            active.setTemplateId(template.getId());
            atActiveService.insert(active);

            //处理参数模板
            String parameters = logWebVo.getParameters();
            JSONObject paramJsonObj = JSON.parseObject(parameters);
            Set<String> keySet = paramJsonObj.keySet();
            for (String key : keySet) {
                AtParameter parameter = new AtParameter();
//                parameter.setName();
                parameter.setCode(key);
//                parameter.setDataType(key);
                //byte 为文件流做准备,之后读取key后，可以修改value
                parameter.setDataValue(paramJsonObj.getString(key).getBytes());
                parameter.setActiveId(active.getId());
                parameter.setRuleType(AtParamterRuleType.statics.getCode());
//                parameter.setScript();
                atParameterService.insert(parameter);
            }
        }
    }

    @Override
    public AtTemplateVo getTemplateVo(Long templteId) {
        //
        AtTemplate template = findById(templteId);
        AtTemplateVo templateVo = BeanUtils.beanCopy(template,AtTemplateVo.class);
        //
        AtActiveQuery activeQuery = new AtActiveQuery();
        activeQuery.setTemplateId(templteId);
        List<AtActive> activeList = atActiveService.selectList(activeQuery);
        List<AtActiveVo> activeVoList = CollectUtils.transList(activeList, AtActiveVo.class);
        if (CollectionUtils.isEmpty(activeVoList)) {
            return templateVo;
        }
        templateVo.setActiveVoList(activeVoList);
        for (AtActiveVo activeVo : activeVoList) {
            //
            AtParameterQuery parameterQuery = new AtParameterQuery();
            parameterQuery.setActiveId(activeVo.getId());
            List<AtParameter> parameterList = atParameterService.selectList(parameterQuery);
            if (CollectionUtils.isEmpty(parameterList)) {
                continue;
            }
            List<AtParameterVo> parameterVoList = CollectUtils.transList(parameterList,AtParameterVo.class);
            activeVo.setParameterVoList(parameterVoList);
        }
        return templateVo;
    }

}
