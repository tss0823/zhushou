package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.dal.mapper.AtActiveInstMapper;
import com.yuntao.zhushou.model.domain.AtActiveInst;
import com.yuntao.zhushou.model.query.AtActiveInstQuery;
import com.yuntao.zhushou.model.vo.AtActiveInstVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.service.inter.AtActiveInstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 活动实例服务实现类
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
@Service("atActiveInstService")
public class AtActiveInstServiceImpl implements AtActiveInstService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AtActiveInstMapper atActiveInstMapper;

    @Override
    public List<AtActiveInst> selectList(AtActiveInstQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return atActiveInstMapper.selectList(queryMap);
    }

    @Override
    public Pagination<AtActiveInstVo> selectPage(AtActiveInstQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = atActiveInstMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<AtActiveInst> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        queryMap.put("pagination",pagination);
        List<AtActiveInst> dataList = atActiveInstMapper.selectList(queryMap);
        Pagination<AtActiveInstVo> newPageInfo = new Pagination<>(pagination);
        List<AtActiveInstVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for(AtActiveInst atActiveInst : dataList){
            AtActiveInstVo atActiveInstVo = BeanUtils.beanCopy(atActiveInst,AtActiveInstVo.class);
            newDataList.add(atActiveInstVo);
        }
        return newPageInfo;

    }

    @Override
    public AtActiveInst findById(Long id) {
        return atActiveInstMapper.findById(id);
    }


    @Override
    public int insert(AtActiveInst atActiveInst) {
        return atActiveInstMapper.insert(atActiveInst);
    }

    @Override
    public int updateById(AtActiveInst atActiveInst) {
        return atActiveInstMapper.updateById(atActiveInst);
    }

    @Override
    public int deleteById(Long id) {
        return atActiveInstMapper.deleteById(id);
    }

}
