/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.AtVariableMapper;
import com.yuntao.zhushou.model.domain.AtVariable;
import com.yuntao.zhushou.model.enums.AtVariableScope;
import com.yuntao.zhushou.model.enums.YesNoIntType;
import com.yuntao.zhushou.model.query.AtVariableQuery;
import com.yuntao.zhushou.model.vo.AtVariableVo;
import com.yuntao.zhushou.service.inter.AtVariableService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("atVariable")
public class AtVariableServiceImpl extends AbstService implements AtVariableService {


    @Autowired
    private AtVariableMapper atVariableMapper;

    @Override
    public List<AtVariable> selectList(AtVariableQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return atVariableMapper.selectList(queryMap);
    }

    @Override
    public AtVariable selectOne(AtVariableQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<AtVariable> atVariables = atVariableMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(atVariables)) {
            return atVariables.get(0);
        }
        return null;
    }

    @Override
    public Pagination<AtVariableVo> selectPage(AtVariableQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = atVariableMapper.selectListCount(queryMap);
        Pagination<AtVariableVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<AtVariable> dataList = atVariableMapper.selectList(queryMap);
        List<AtVariableVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (AtVariable atVariable : dataList) {
            AtVariableVo atVariableVo = BeanUtils.beanCopy(atVariable, AtVariableVo.class);
            newDataList.add(atVariableVo);
        }
        return pagination;
    }

    @Override
    public AtVariable findById(Long id) {
        return atVariableMapper.findById(id);
    }


    @Override
    public int insert(AtVariable atVariable) {
        return atVariableMapper.insert(atVariable);
    }

    @Override
    public int updateById(AtVariable atVariable) {
        return atVariableMapper.updateById(atVariable);
    }

    @Override
    public int deleteById(Long id) {
        return atVariableMapper.deleteById(id);
    }

    @Override
    public List<AtVariable> getGlobalList() {
        AtVariableQuery query = new AtVariableQuery();
        query.setStatus(YesNoIntType.yes.getCode());
        query.setScope(AtVariableScope.global.getCode());
        return this.selectList(query);
    }

    @Override
    public List<AtVariable> getPriList(Long templateId) {
        AtVariableQuery query = new AtVariableQuery();
        query.setStatus(YesNoIntType.yes.getCode());
        query.setScope(AtVariableScope.global.getCode());
        query.setTemplateId(templateId);
        return this.selectList(query);
    }

    @Override
    public Map<String,Object> getVariableMap(Long templateId) {
        List<AtVariable> priList = this.getPriList(templateId);
        List<AtVariable> globalList = this.getGlobalList();
        Map<String,AtVariable> variableMap = new HashMap<>();
        for (AtVariable atVariable : globalList) {
            variableMap.put(atVariable.getKey(),atVariable);
        }
        for (AtVariable atVariable : priList) {
            variableMap.put(atVariable.getKey(),atVariable);
        }
        Collection<AtVariable> values = variableMap.values();
        Map<String,Object> resultMap = new HashMap<>();
        for (AtVariable value : values) {
            resultMap.put(value.getKey(),value.getValue());
        }
        return resultMap;
    }


}