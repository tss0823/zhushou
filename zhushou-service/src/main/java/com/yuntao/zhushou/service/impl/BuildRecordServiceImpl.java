/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.BuildRecordMapper;
import com.yuntao.zhushou.model.domain.BuildRecord;
import com.yuntao.zhushou.model.query.BuildRecordQuery;
import com.yuntao.zhushou.model.vo.BuildRecordVo;
import com.yuntao.zhushou.service.inter.BuildRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class BuildRecordServiceImpl extends AbstService implements BuildRecordService {


    @Autowired
    private BuildRecordMapper buildRecordMapper;

    @Override
    public List<BuildRecord> selectList(BuildRecordQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return buildRecordMapper.selectList(queryMap);
    }

    @Override
    public BuildRecord selectOne(BuildRecordQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<BuildRecord> buildRecords = buildRecordMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(buildRecords)) {
            return buildRecords.get(0);
        }
        return null;
    }

    @Override
    public Pagination<BuildRecordVo> selectPage(BuildRecordQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = buildRecordMapper.selectListCount(queryMap);
        Pagination<BuildRecordVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<BuildRecord> dataList = buildRecordMapper.selectList(queryMap);
        List<BuildRecordVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (BuildRecord buildRecord : dataList) {
            BuildRecordVo buildRecordVo = BeanUtils.beanCopy(buildRecord, BuildRecordVo.class);
            newDataList.add(buildRecordVo);
        }
        return pagination;
    }

    @Override
    public BuildRecord findById(Long id) {
        return buildRecordMapper.findById(id);
    }


    @Override
    public int insert(BuildRecord buildRecord) {
        return buildRecordMapper.insert(buildRecord);
    }

    @Override
    public int updateById(BuildRecord buildRecord) {
        return buildRecordMapper.updateById(buildRecord);
    }

    @Override
    public int deleteById(Long id) {
        return buildRecordMapper.deleteById(id);
    }


}