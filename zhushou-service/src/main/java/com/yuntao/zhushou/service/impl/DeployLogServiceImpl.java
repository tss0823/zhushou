package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.dal.mapper.DeployLogMapper;
import com.yuntao.zhushou.model.domain.DeployLog;
import com.yuntao.zhushou.model.query.DeployLogQuery;
import com.yuntao.zhushou.model.vo.DeployLogVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.service.inter.DeployLogService;
import org.jsoup.helper.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shan on 2016/3/27.
 */
@Service
public class DeployLogServiceImpl extends AbstService implements DeployLogService {

    @Autowired
    private DeployLogMapper deployLogMapper;

    @Override
    public DeployLog findById(Long id) {
        return deployLogMapper.findById(id);
    }

    @Override
    public DeployLogVo findDetailById(Long id) {
        DeployLogVo deployLogVo = deployLogMapper.findDetailById(id);
        byte log [] = deployLogVo.getLog();
        if(log != null){
            deployLogVo.setLogText(new String(log));
        }
        deployLogVo.setLastTime(DateUtil.getRangeTime(deployLogVo.getGmtCreate()));
        return deployLogVo;

    }


    @Override
    public Pagination<DeployLogVo> selectPage(DeployLogQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = deployLogMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<DeployLog> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        queryMap.put("pagination",pagination);
        List<DeployLog> dataList = deployLogMapper.selectList(queryMap);
        Pagination<DeployLogVo> newPageInfo = new Pagination<>(pagination);
        List<DeployLogVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for(DeployLog deployLog : dataList){
            DeployLogVo deployLogVo = BeanUtils.beanCopy(deployLog,DeployLogVo.class);
            deployLogVo.setLastTime(DateUtil.getRangeTime(deployLogVo.getGmtCreate()));
            deployLogVo.setTime(DateUtil.getFmt(deployLogVo.getGmtCreate().getTime(),"yyyy-MM-dd HH:mm:ss"));
            newDataList.add(deployLogVo);
        }
        return newPageInfo;
    }

    @Override
    public int insert(DeployLog deployLog) {
        return deployLogMapper.insert(deployLog);
    }

    @Override
    public List<DeployLog> selectList(DeployLogQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return deployLogMapper.selectList(queryMap);
    }
}
