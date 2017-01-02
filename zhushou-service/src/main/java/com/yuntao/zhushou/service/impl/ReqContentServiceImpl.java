package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.dal.mapper.ReqContentMapper;
import com.yuntao.zhushou.model.domain.ReqContent;
import com.yuntao.zhushou.model.query.ReqContentQuery;
import com.yuntao.zhushou.model.vo.ReqContentVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.service.inter.ReqContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 请求内容服务实现类
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
@Service("reqContentService")
public class ReqContentServiceImpl implements ReqContentService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReqContentMapper reqContentMapper;

    @Override
    public List<ReqContent> selectList(ReqContentQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return reqContentMapper.selectList(queryMap);
    }

    @Override
    public Pagination<ReqContentVo> selectPage(ReqContentQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = reqContentMapper.selectListCount(queryMap);
        Pagination<ReqContentVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination",pagination);
        List<ReqContent> dataList = reqContentMapper.selectList(queryMap);
//        Pagination<ReqContentVo> newPageInfo = new Pagination<>(pagination);
        List<ReqContentVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
//        pagination.setDataList(dataList);
        for(ReqContent reqContent : dataList){
            ReqContentVo reqContentVo = BeanUtils.beanCopy(reqContent,ReqContentVo.class);
            newDataList.add(reqContentVo);
        }
        return pagination;

    }

    @Override
    public ReqContent findById(Long id) {
        return reqContentMapper.findById(id);
    }


    @Override
    public int insert(ReqContent reqContent) {
        return reqContentMapper.insert(reqContent);
    }

    @Override
    public int updateById(ReqContent reqContent) {
        return reqContentMapper.updateById(reqContent);
    }

    @Override
    public int deleteById(Long id) {
        return reqContentMapper.deleteById(id);
    }

}
