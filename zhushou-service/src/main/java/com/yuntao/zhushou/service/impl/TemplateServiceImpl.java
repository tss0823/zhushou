/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.TemplateMapper;
import com.yuntao.zhushou.model.domain.Template;
import com.yuntao.zhushou.model.query.TemplateQuery;
import com.yuntao.zhushou.model.vo.TemplateVo;
import com.yuntao.zhushou.service.inter.TemplateService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class TemplateServiceImpl extends AbstService implements TemplateService {


    @Autowired
    private TemplateMapper templateMapper;

    @Override
    public List<Template> selectList(TemplateQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return templateMapper.selectList(queryMap);
    }

    @Override
    public Template selectOne(TemplateQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<Template> templates = templateMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(templates)) {
            return templates.get(0);
        }
        return null;
    }

    @Override
    public Pagination<TemplateVo> selectPage(TemplateQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = templateMapper.selectListCount(queryMap);
        Pagination<TemplateVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<Template> dataList = templateMapper.selectList(queryMap);
        List<TemplateVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (Template template : dataList) {
            TemplateVo templateVo = BeanUtils.beanCopy(template, TemplateVo.class);
            newDataList.add(templateVo);
        }
        return pagination;
    }

    @Override
    public Template findById(Long id) {
        return templateMapper.findById(id);
    }


    @Override
    public int insert(Template template) {
        return templateMapper.insert(template);
    }

    @Override
    public int updateById(Template template) {
        return templateMapper.updateById(template);
    }

    @Override
    public int deleteById(Long id) {
        return templateMapper.deleteById(id);
    }


}