package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.dal.mapper.ProxyContentMapper;
import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.enums.ProxyContentStatus;
import com.yuntao.zhushou.model.query.ProxyContentQuery;
import com.yuntao.zhushou.model.vo.ProxyContentVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.service.inter.ProxyContentService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 请求内容服务实现类
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
@Service("proxyContentService")
public class ProxyContentServiceImpl extends AbstService implements ProxyContentService {

    @Autowired
    private ProxyContentMapper proxyContentMapper;


    @Override
    public List<ProxyContent> selectList(ProxyContentQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return proxyContentMapper.selectList(queryMap);
    }

    @Override
    public Pagination<ProxyContentVo> selectPage(ProxyContentQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = proxyContentMapper.selectListCount(queryMap);
        Pagination<ProxyContentVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination",pagination);
        List<ProxyContent> dataList = proxyContentMapper.selectList(queryMap);
        List<ProxyContentVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for(ProxyContent reqContent : dataList){
            ProxyContentVo reqContentVo = BeanUtils.beanCopy(reqContent,ProxyContentVo.class);
            ProxyContentStatus proxyContentStatus = ProxyContentStatus.getByCode(reqContentVo.getStatus());
            if(proxyContentStatus != null){
                reqContentVo.setStatusText(proxyContentStatus.getDescription());
            }
            reqContentVo.setLastReqTime(DateUtil.getRangeTime(reqContentVo.getGmtRequest()));
            reqContentVo.setLastResTime(DateUtil.getRangeTime(reqContentVo.getGmtResponse()));

            newDataList.add(reqContentVo);
        }
        return pagination;

    }

    @Override
    public ProxyContent findById(Long id) {
        return proxyContentMapper.findById(id);
    }


    @Override
    public int insert(ProxyContent reqContent) {
        return proxyContentMapper.insert(reqContent);
    }

    @Override
    public int updateById(ProxyContent reqContent) {
        return proxyContentMapper.updateById(reqContent);
    }

    @Override
    public int deleteById(Long id) {
        return proxyContentMapper.deleteById(id);
    }

    @Override
    public void insertBatch(List<ProxyContent> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        //批量操作业务300条一次
        int maxBatchSize = 300;
        int fromIndex = 0;
        int toIndex = dataList.size() > maxBatchSize ? maxBatchSize : dataList.size();
        List<ProxyContent> subDataList = dataList.subList(fromIndex, toIndex);
        while(toIndex <= dataList.size()){
            proxyContentMapper.insertBatch(subDataList);
            if(toIndex == dataList.size()){
                break;
            }
            fromIndex = toIndex;
            toIndex = fromIndex + maxBatchSize;
            if(toIndex > dataList.size()){
                toIndex = dataList.size();
            }
            subDataList = dataList.subList(fromIndex, toIndex);
        }
    }

}
