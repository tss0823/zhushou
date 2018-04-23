package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.cache.CacheService;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.HostMapper;
import com.yuntao.zhushou.model.domain.AppHost;
import com.yuntao.zhushou.model.domain.Host;
import com.yuntao.zhushou.model.enums.YesNoIntType;
import com.yuntao.zhushou.model.query.HostQuery;
import com.yuntao.zhushou.service.inter.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by shan on 2016/3/27.
 */
@Service
public class HostServiceImpl extends AbstService implements HostService {

    @Autowired
    private HostMapper hostMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    public Host findById(Long id) {
        return hostMapper.findById(id);
    }


    @Override
    public Pagination<Host> selectPage(HostQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = hostMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return new Pagination<>();
        }
        Pagination<Host> pageInfo = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        List<Host> dataList = hostMapper.selectList(queryMap);
        pageInfo.setDataList(dataList);
        return pageInfo;
    }

    @Override
    public List<Host> selectListByAppAndModel(Long appId, String model) {
        //get form cache
//        String key = CacheConstant.Host.selectListByAll+"_"+model+"_"+appId;
//        List<Host> dataList = (List<Host>) cacheService.get(key);
//        if(CollectionUtils.isNotEmpty(dataList)){
//            return dataList;
//        }
        List<Host> dataList = hostMapper.selectListByAppAndModel(appId,model);
        //set to cache
//        cacheService.set(key,dataList);
        return dataList;
    }

    @Override
    public List<Host> selectListByAppId(Long appId) {
        //get form cache
//        String key = CacheConstant.Host.selectListByAll+"_"+appId;
//        List<Host> dataList = (List<Host>) cacheService.get(key);
//        if(CollectionUtils.isNotEmpty(dataList)){
//            return dataList;
//        }
        return  hostMapper.selectListByAppId(appId);
        //set to cache
//        cacheService.set(key,dataList);
//        return dataList;
    }

    @Override
    public List<Host> selectList(HostQuery query) {
        //get form cache
//        String queryString = BeanUtils.beanToString(query);
//        String key = CacheConstant.Host.selectListByAll+"_"+queryString;
//        List<Host> dataList = (List<Host>) cacheService.get(key);
//        if(CollectionUtils.isNotEmpty(dataList)){
//            return dataList;
//        }
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return hostMapper.selectList(queryMap);
        //set to cache
//        cacheService.set(key,dataList);
//        return dataList;
    }

    @Override
    public int insert(Host host) {
        host.setType(YesNoIntType.yes.getCode());
        host.setStatus(YesNoIntType.yes.getCode());
        return hostMapper.insert(host);
    }

    @Override
    public int updateById(Host host) {
        return hostMapper.updateById(host);
    }

    @Override
    public int deleteById(Long id) {
        return hostMapper.deleteById(id);
    }

    @Override
    public int deleteAppHostByAppId(Long appId) {
        return hostMapper.deleteAppHostByAppId(appId);
    }

    @Override
    public int insertAppHostBatch(List<AppHost> dataList) {
        return hostMapper.insertAppHostBatch(dataList);
    }

}
