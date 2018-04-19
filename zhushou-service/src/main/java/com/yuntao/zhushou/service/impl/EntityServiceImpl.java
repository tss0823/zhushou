/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.EntityMapper;
import com.yuntao.zhushou.model.domain.Entity;
import com.yuntao.zhushou.model.query.EntityQuery;
import com.yuntao.zhushou.model.vo.EntityVo;
import com.yuntao.zhushou.service.inter.EntityService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class EntityServiceImpl extends AbstService implements EntityService {


    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<Entity> selectList(EntityQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return entityMapper.selectList(queryMap);
    }

    @Override
    public Entity selectOne(EntityQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<Entity> entitys = entityMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(entitys)) {
            return entitys.get(0);
        }
        return null;
    }

    @Override
    public Pagination<EntityVo> selectPage(EntityQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = entityMapper.selectListCount(queryMap);
        Pagination<EntityVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<Entity> dataList = entityMapper.selectList(queryMap);
        List<EntityVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (Entity entity : dataList) {
            EntityVo entityVo = BeanUtils.beanCopy(entity, EntityVo.class);
            newDataList.add(entityVo);
        }
        return pagination;
    }

    @Override
    public Entity findById(Long id) {
        return entityMapper.findById(id);
    }


    @Override
    public int insert(Entity entity) {
        return entityMapper.insert(entity);
    }

    @Override
    public int updateById(Entity entity) {
        return entityMapper.updateById(entity);
    }

    @Override
    public int deleteById(Long id) {
        return entityMapper.deleteById(id);
    }


}