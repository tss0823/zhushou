/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.MarkMapper;
import com.yuntao.zhushou.model.domain.Mark;
import com.yuntao.zhushou.model.query.MarkQuery;
import com.yuntao.zhushou.model.vo.MarkVo;
import com.yuntao.zhushou.model.vo.mark.MarkTopNVo;
import com.yuntao.zhushou.service.inter.MarkService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("mark")
public class MarkServiceImpl extends AbstService implements MarkService {


    @Autowired
    private MarkMapper markMapper;

    @Override
    public List<Mark> selectList(MarkQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return markMapper.selectList(queryMap);
    }

    @Override
    public Mark selectOne(MarkQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<Mark> marks = markMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(marks)) {
            return marks.get(0);
        }
        return null;
    }

    @Override
    public Pagination<MarkVo> selectPage(MarkQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = markMapper.selectListCount(queryMap);
        Pagination<MarkVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<Mark> dataList = markMapper.selectList(queryMap);
        List<MarkVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (Mark mark : dataList) {
            MarkVo markVo = BeanUtils.beanCopy(mark, MarkVo.class);
            newDataList.add(markVo);
        }
        return pagination;
    }

    @Override
    public Mark findById(Long id) {
        return markMapper.findById(id);
    }


    @Override
    public int insert(Mark mark) {
        return markMapper.insert(mark);
    }

    @Override
    public int updateById(Mark mark) {
        return markMapper.updateById(mark);
    }

    @Override
    public int deleteById(Long id) {
        return markMapper.deleteById(id);
    }

    @Override
    public List<MarkTopNVo> selectTopN(Integer type, Integer startPeriods, Integer endPeriods, String ascOrDesc, Integer topN) {
        return markMapper.selectTopN(type,startPeriods,endPeriods,ascOrDesc,topN);
    }

    @Override
    public int selectLastLocation(Integer endPeriods,Integer val) {
        return markMapper.selectLastLocation(endPeriods,val);
    }

    @Override
    public int selectLastItemIndex(Integer endPeriods) {
        return markMapper.selectLastItemIndex(endPeriods);
    }


}