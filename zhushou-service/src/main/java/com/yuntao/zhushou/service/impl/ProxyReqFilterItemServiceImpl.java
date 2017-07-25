/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.ProxyReqFilterItemMapper;
import com.yuntao.zhushou.model.domain.ProxyReqFilterItem;
import com.yuntao.zhushou.model.query.ProxyReqFilterItemQuery;
import com.yuntao.zhushou.model.vo.ProxyReqFilterItemVo;
import com.yuntao.zhushou.service.inter.ProxyReqFilterItemService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ProxyReqFilterItemServiceImpl extends AbstService implements ProxyReqFilterItemService {


    @Autowired
    private ProxyReqFilterItemMapper proxyReqFilterItemMapper;

    @Override
    public List<ProxyReqFilterItem> selectList(ProxyReqFilterItemQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return proxyReqFilterItemMapper.selectList(queryMap);
    }

    @Override
    public ProxyReqFilterItem selectOne(ProxyReqFilterItemQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<ProxyReqFilterItem> proxyReqFilterItems = proxyReqFilterItemMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(proxyReqFilterItems)) {
            return proxyReqFilterItems.get(0);
        }
        return null;
    }

    @Override
    public Pagination<ProxyReqFilterItemVo> selectPage(ProxyReqFilterItemQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = proxyReqFilterItemMapper.selectListCount(queryMap);
        Pagination<ProxyReqFilterItemVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<ProxyReqFilterItem> dataList = proxyReqFilterItemMapper.selectList(queryMap);
        List<ProxyReqFilterItemVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (ProxyReqFilterItem proxyReqFilterItem : dataList) {
            ProxyReqFilterItemVo proxyReqFilterItemVo = BeanUtils.beanCopy(proxyReqFilterItem, ProxyReqFilterItemVo.class);
            newDataList.add(proxyReqFilterItemVo);
        }
        return pagination;
    }

    @Override
    public ProxyReqFilterItem findById(Long id) {
        return proxyReqFilterItemMapper.findById(id);
    }


    @Override
    public int insert(ProxyReqFilterItem proxyReqFilterItem) {
        return proxyReqFilterItemMapper.insert(proxyReqFilterItem);
    }

    @Override
    public int updateById(ProxyReqFilterItem proxyReqFilterItem) {
        return proxyReqFilterItemMapper.updateById(proxyReqFilterItem);
    }

    @Override
    public int deleteById(Long id) {
        return proxyReqFilterItemMapper.deleteById(id);
    }

    @Override
    public List<ProxyReqFilterItem> selectByParentId(Integer filterType,Long parentId) {
        ProxyReqFilterItemQuery query = new ProxyReqFilterItemQuery();
        query.setParentId(parentId);
        query.setFilterType(filterType);
        return this.selectList(query);
    }


}