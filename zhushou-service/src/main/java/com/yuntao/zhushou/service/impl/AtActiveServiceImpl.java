package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.dal.mapper.AtActiveMapper;
import com.yuntao.zhushou.model.domain.AtActive;
import com.yuntao.zhushou.model.query.AtActiveQuery;
import com.yuntao.zhushou.model.vo.AtActiveVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.service.inter.AtActiveService;
import org.jsoup.helper.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 活动模板服务实现类
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
@Service("atActiveService")
public class AtActiveServiceImpl implements AtActiveService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AtActiveMapper atActiveMapper;

    @Override
    public List<AtActive> selectList(AtActiveQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return atActiveMapper.selectList(queryMap);
    }

    @Override
    public Pagination<AtActiveVo> selectPage(AtActiveQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = atActiveMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<AtActive> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        queryMap.put("pagination",pagination);
        List<AtActive> dataList = atActiveMapper.selectList(queryMap);
        Pagination<AtActiveVo> newPageInfo = new Pagination<>(pagination);
        List<AtActiveVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for(AtActive atActive : dataList){
            AtActiveVo atActiveVo = BeanUtils.beanCopy(atActive,AtActiveVo.class);
            newDataList.add(atActiveVo);
        }
        return newPageInfo;

    }

    @Override
    public AtActive findById(Long id) {
        return atActiveMapper.findById(id);
    }


    @Override
    public int insert(AtActive atActive) {
        return atActiveMapper.insert(atActive);
    }

    @Override
    public int updateById(AtActive atActive) {
        return atActiveMapper.updateById(atActive);
    }

    @Override
    public int deleteById(Long id) {
        return atActiveMapper.deleteById(id);
    }

}
