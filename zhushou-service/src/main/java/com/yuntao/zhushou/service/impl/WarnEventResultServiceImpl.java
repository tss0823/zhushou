/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.WarnEventResultMapper;
import com.yuntao.zhushou.model.domain.WarnEventResult;
import com.yuntao.zhushou.model.query.WarnEventResultQuery;
import com.yuntao.zhushou.model.vo.WarnEventResultVo;
import com.yuntao.zhushou.service.inter.WarnEventResultService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("warnEventResultService")
public class WarnEventResultServiceImpl extends AbstService implements WarnEventResultService {


    @Autowired
    private WarnEventResultMapper warnEventResultMapper;

    @Override
    public List<WarnEventResult> selectList(WarnEventResultQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return warnEventResultMapper.selectList(queryMap);
    }

    @Override
    public WarnEventResult selectOne(WarnEventResultQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<WarnEventResult> warnEventResults = warnEventResultMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(warnEventResults)) {
            return warnEventResults.get(0);
        }
        return null;
    }

    @Override
    public Pagination<WarnEventResultVo> selectPage(WarnEventResultQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = warnEventResultMapper.selectListCount(queryMap);
        Pagination<WarnEventResultVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<WarnEventResult> dataList = warnEventResultMapper.selectList(queryMap);
        List<WarnEventResultVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (WarnEventResult warnEventResult : dataList) {
            WarnEventResultVo warnEventResultVo = BeanUtils.beanCopy(warnEventResult, WarnEventResultVo.class);
            newDataList.add(warnEventResultVo);
        }
        return pagination;
    }

    @Override
    public WarnEventResult findById(Long id) {
        return warnEventResultMapper.findById(id);
    }


    @Override
    public int insert(WarnEventResult warnEventResult) {
        return warnEventResultMapper.insert(warnEventResult);
    }

    @Override
    public int updateById(WarnEventResult warnEventResult) {
        return warnEventResultMapper.updateById(warnEventResult);
    }

    @Override
    public int deleteById(Long id) {
        return warnEventResultMapper.deleteById(id);
    }


}