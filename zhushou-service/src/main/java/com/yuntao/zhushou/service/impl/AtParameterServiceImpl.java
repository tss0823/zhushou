package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.dal.mapper.AtParameterMapper;
import com.yuntao.zhushou.model.domain.AtParameter;
import com.yuntao.zhushou.model.query.AtParameterQuery;
import com.yuntao.zhushou.model.vo.AtParameterVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.service.inter.AtParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 模板参数服务实现类
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
@Service("atParameterService")
public class AtParameterServiceImpl implements AtParameterService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AtParameterMapper atParameterMapper;

    @Override
    public List<AtParameter> selectList(AtParameterQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return atParameterMapper.selectList(queryMap);
    }

    @Override
    public Pagination<AtParameterVo> selectPage(AtParameterQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = atParameterMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<AtParameter> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        queryMap.put("pagination",pagination);
        List<AtParameter> dataList = atParameterMapper.selectList(queryMap);
        Pagination<AtParameterVo> newPageInfo = new Pagination<>(pagination);
        List<AtParameterVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for(AtParameter atParameter : dataList){
            AtParameterVo atParameterVo = BeanUtils.beanCopy(atParameter,AtParameterVo.class);
            newDataList.add(atParameterVo);
        }
        return newPageInfo;

    }

    @Override
    public AtParameter findById(Long id) {
        return atParameterMapper.findById(id);
    }


    @Override
    public int insert(AtParameter atParameter) {
        return atParameterMapper.insert(atParameter);
    }

    @Override
    public int updateById(AtParameter atParameter) {
        return atParameterMapper.updateById(atParameter);
    }

    @Override
    public int deleteById(Long id) {
        return atParameterMapper.deleteById(id);
    }

}
