/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.PropertyMapper;
import com.yuntao.zhushou.model.domain.Property;
import com.yuntao.zhushou.model.query.PropertyQuery;
import com.yuntao.zhushou.model.vo.PropertyVo;
import com.yuntao.zhushou.service.inter.PropertyService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class PropertyServiceImpl extends AbstService implements PropertyService {


    @Autowired
    private PropertyMapper propertyMapper;

    @Override
    public List<Property> selectList(PropertyQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return propertyMapper.selectList(queryMap);
    }

    @Override
    public Property selectOne(PropertyQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<Property> propertys = propertyMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(propertys)) {
            return propertys.get(0);
        }
        return null;
    }

    @Override
    public Pagination<PropertyVo> selectPage(PropertyQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = propertyMapper.selectListCount(queryMap);
        Pagination<PropertyVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<Property> dataList = propertyMapper.selectList(queryMap);
        List<PropertyVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (Property property : dataList) {
            PropertyVo propertyVo = BeanUtils.beanCopy(property, PropertyVo.class);
            newDataList.add(propertyVo);
        }
        return pagination;
    }

    @Override
    public Property findById(Long id) {
        return propertyMapper.findById(id);
    }


    @Override
    public int insert(Property property) {
        return propertyMapper.insert(property);
    }

    @Override
    public int updateById(Property property) {
        return propertyMapper.updateById(property);
    }

    @Override
    public int deleteById(Long id) {
        return propertyMapper.deleteById(id);
    }

    @Override
    public int insertBatch(Long entityId,List<Property> dataList) {
        //delete old
        propertyMapper.deleteByEntityId(entityId);

        for (Property property : dataList) {
            property.setEntityId(entityId);
        }
        return propertyMapper.insertBatch(dataList);
    }


}