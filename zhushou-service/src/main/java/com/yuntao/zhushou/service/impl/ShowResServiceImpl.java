package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.dal.mapper.ShowResMapper;
import com.yuntao.zhushou.model.domain.ShowRes;
import com.yuntao.zhushou.model.query.ShowResQuery;
import com.yuntao.zhushou.model.vo.ShowResVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.service.inter.ShowResService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 展示资源服务实现类
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
@Service("showResService")
public class ShowResServiceImpl implements ShowResService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShowResMapper showResMapper;

    @Override
    public List<ShowRes> selectList(ShowResQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return showResMapper.selectList(queryMap);
    }

    @Override
    public Pagination<ShowResVo> selectPage(ShowResQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = showResMapper.selectListCount(queryMap);
        Pagination<ShowResVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination",pagination);
        List<ShowRes> dataList = showResMapper.selectList(queryMap);
//        Pagination<ShowResVo> newPageInfo = new Pagination<>(pagination);
        List<ShowResVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
//        pagination.setDataList(dataList);
        for(ShowRes showRes : dataList){
            ShowResVo showResVo = BeanUtils.beanCopy(showRes,ShowResVo.class);
            newDataList.add(showResVo);
        }
        return pagination;

    }

    @Override
    public ShowRes findById(Long id) {
        return showResMapper.findById(id);
    }


    @Override
    public int insert(ShowRes showRes) {
        return showResMapper.insert(showRes);
    }

    @Override
    public int updateById(ShowRes showRes) {
        return showResMapper.updateById(showRes);
    }

    @Override
    public int deleteById(Long id) {
        return showResMapper.deleteById(id);
    }

}
