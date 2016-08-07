package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.dal.mapper.RoleAuthResMapper;
import com.yuntao.zhushou.model.domain.RoleAuthRes;
import com.yuntao.zhushou.model.query.RoleAuthResQuery;
import com.yuntao.zhushou.model.vo.RoleAuthResVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.service.inter.RoleAuthResService;
import org.jsoup.helper.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 角色权限资源服务实现类
 * 
 * @author admin
 *
 * @2016-08-07 11
 */
@Service("roleAuthResService")
public class RoleAuthResServiceImpl implements RoleAuthResService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleAuthResMapper roleAuthResMapper;

    @Override
    public List<RoleAuthRes> selectList(RoleAuthResQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return roleAuthResMapper.selectList(queryMap);
    }

    @Override
    public Pagination<RoleAuthResVo> selectPage(RoleAuthResQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = roleAuthResMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<RoleAuthRes> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        queryMap.put("pagination",pagination);
        List<RoleAuthRes> dataList = roleAuthResMapper.selectList(queryMap);
        Pagination<RoleAuthResVo> newPageInfo = new Pagination<>(pagination);
        List<RoleAuthResVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for(RoleAuthRes roleAuthRes : dataList){
            RoleAuthResVo roleAuthResVo = BeanUtils.beanCopy(roleAuthRes,RoleAuthResVo.class);
            newDataList.add(roleAuthResVo);
        }
        return newPageInfo;

    }

    @Override
    public RoleAuthRes findById(Long id) {
        return roleAuthResMapper.findById(id);
    }


    @Override
    public int insert(RoleAuthRes roleAuthRes) {
        return roleAuthResMapper.insert(roleAuthRes);
    }

    @Override
    public int updateById(RoleAuthRes roleAuthRes) {
        return roleAuthResMapper.updateById(roleAuthRes);
    }

    @Override
    public int deleteById(Long id) {
        return roleAuthResMapper.deleteById(id);
    }

}
