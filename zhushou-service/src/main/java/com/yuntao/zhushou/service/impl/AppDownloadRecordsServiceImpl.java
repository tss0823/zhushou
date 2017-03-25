/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.AppDownloadRecordsMapper;
import com.yuntao.zhushou.model.domain.AppDownloadRecords;
import com.yuntao.zhushou.model.query.AppDownloadRecordsQuery;
import com.yuntao.zhushou.model.vo.AppDownloadRecordsVo;
import com.yuntao.zhushou.service.inter.AppDownloadRecordsService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("appDownloadRecords")
public class AppDownloadRecordsServiceImpl extends AbstService implements AppDownloadRecordsService {


    @Autowired
    private AppDownloadRecordsMapper appDownloadRecordsMapper;

    @Override
    public List<AppDownloadRecords> selectList(AppDownloadRecordsQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return appDownloadRecordsMapper.selectList(queryMap);
    }

    @Override
    public AppDownloadRecords selectOne(AppDownloadRecordsQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<AppDownloadRecords> appDownloadRecordss = appDownloadRecordsMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(appDownloadRecordss)) {
            return appDownloadRecordss.get(0);
        }
        return null;
    }

    @Override
    public Pagination<AppDownloadRecordsVo> selectPage(AppDownloadRecordsQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = appDownloadRecordsMapper.selectListCount(queryMap);
        Pagination<AppDownloadRecordsVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<AppDownloadRecords> dataList = appDownloadRecordsMapper.selectList(queryMap);
        List<AppDownloadRecordsVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (AppDownloadRecords appDownloadRecords : dataList) {
            AppDownloadRecordsVo appDownloadRecordsVo = BeanUtils.beanCopy(appDownloadRecords, AppDownloadRecordsVo.class);
            newDataList.add(appDownloadRecordsVo);
        }
        return pagination;
    }

    @Override
    public AppDownloadRecords findById(Long id) {
        return appDownloadRecordsMapper.findById(id);
    }


    @Override
    public int insert(AppDownloadRecords appDownloadRecords) {
        return appDownloadRecordsMapper.insert(appDownloadRecords);
    }

    @Override
    public int updateById(AppDownloadRecords appDownloadRecords) {
        return appDownloadRecordsMapper.updateById(appDownloadRecords);
    }

    @Override
    public int deleteById(Long id) {
        return appDownloadRecordsMapper.deleteById(id);
    }


}