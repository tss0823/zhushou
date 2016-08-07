package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.dal.mapper.RoleMapper;
import com.yuntao.zhushou.model.domain.Role;
import com.yuntao.zhushou.model.query.RoleQuery;
import com.yuntao.zhushou.model.vo.RoleVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.service.inter.RoleService;
import org.jsoup.helper.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 角色服务实现类
 * 
 * @author admin
 *
 * @2016-08-07 11
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> selectList(RoleQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return roleMapper.selectList(queryMap);
    }

    @Override
    public Pagination<RoleVo> selectPage(RoleQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = roleMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<Role> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        queryMap.put("pagination",pagination);
        List<Role> dataList = roleMapper.selectList(queryMap);
        Pagination<RoleVo> newPageInfo = new Pagination<>(pagination);
        List<RoleVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for(Role role : dataList){
            RoleVo roleVo = BeanUtils.beanCopy(role,RoleVo.class);
            newDataList.add(roleVo);
        }
        return newPageInfo;

    }

    @Override
    public Role findById(Long id) {
        return roleMapper.findById(id);
    }


    @Override
    public int insert(Role role) {
        return roleMapper.insert(role);
    }

    @Override
    public int updateById(Role role) {
        return roleMapper.updateById(role);
    }

    @Override
    public int deleteById(Long id) {
        return roleMapper.deleteById(id);
    }

}
