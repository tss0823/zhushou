package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.dal.mapper.ProxyContentMapper;
import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.query.ProxyContentQuery;
import com.yuntao.zhushou.model.vo.ProxyContentVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.service.inter.ProxyContentService;
import com.yuntao.zhushou.service.inter.ProxyContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service("proxyContentService")
public class ProxyContentServiceImpl implements ProxyContentService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProxyContentMapper reqContentMapper;

    @Override
    public List<ProxyContent> selectList(ProxyContentQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return reqContentMapper.selectList(queryMap);
    }

    @Override
    public Pagination<ProxyContentVo> selectPage(ProxyContentQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = reqContentMapper.selectListCount(queryMap);
        Pagination<ProxyContentVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination",pagination);
        List<ProxyContent> dataList = reqContentMapper.selectList(queryMap);
//        Pagination<ProxyContentVo> newPageInfo = new Pagination<>(pagination);
        List<ProxyContentVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
//        pagination.setDataList(dataList);
        for(ProxyContent reqContent : dataList){
            ProxyContentVo reqContentVo = BeanUtils.beanCopy(reqContent,ProxyContentVo.class);
            newDataList.add(reqContentVo);
        }
        return pagination;

    }

    @Override
    public ProxyContent findById(Long id) {
        return reqContentMapper.findById(id);
    }


    @Override
    public int insert(ProxyContent reqContent) {
        return reqContentMapper.insert(reqContent);
    }

    @Override
    public int updateById(ProxyContent reqContent) {
        return reqContentMapper.updateById(reqContent);
    }

    @Override
    public int deleteById(Long id) {
        return reqContentMapper.deleteById(id);
    }

}
