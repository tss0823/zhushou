package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.dal.mapper.AuthResMapper;
import com.yuntao.zhushou.model.domain.AuthRes;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.AuthResQuery;
import com.yuntao.zhushou.model.vo.AuthResVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.service.inter.AuthResService;
import com.yuntao.zhushou.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 权限资源服务实现类
 * 
 * @author admin
 *
 * @2016-08-07 11
 */
@Service("authResService")
public class AuthResServiceImpl implements AuthResService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthResMapper authResMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<AuthRes> selectList(AuthResQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return authResMapper.selectList(queryMap);
    }

    @Override
    public Pagination<AuthResVo> selectPage(AuthResQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = authResMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return new Pagination<>();
        }
        Pagination<AuthRes> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        queryMap.put("pagination",pagination);
        List<AuthRes> dataList = authResMapper.selectList(queryMap);
        Pagination<AuthResVo> newPageInfo = new Pagination<>(pagination);
        List<AuthResVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for(AuthRes authRes : dataList){
            AuthResVo authResVo = BeanUtils.beanCopy(authRes,AuthResVo.class);
            newDataList.add(authResVo);
        }
        return newPageInfo;

    }

    @Override
    public AuthRes findById(Long id) {
        return authResMapper.findById(id);
    }


    @Override
    public int insert(AuthRes authRes) {
        return authResMapper.insert(authRes);
    }

    @Override
    public int updateById(AuthRes authRes) {
        return authResMapper.updateById(authRes);
    }

    @Override
    public int deleteById(Long id) {
        return authResMapper.deleteById(id);
    }

    @Override
    public List<AuthResVo> selectByUserId(Long userId) {
        User user = userService.findById(userId);
        String[] roleArray = user.getRole().split(",");
        List<Long> roleIds = new ArrayList<>();
        for (String roleId : roleArray) {
            roleIds.add(Long.valueOf(roleId));
        }
        List<AuthRes> authResList = authResMapper.selectByRole(roleIds);
        //获取menu
        List<AuthResVo> menuAuthResList = new ArrayList<>();
        for (AuthRes authRes : authResList) {
            if (authRes.getMenu()) {
                AuthResVo authResVo = BeanUtils.beanCopy(authRes, AuthResVo.class);
                menuAuthResList.add(authResVo);
                //处理menu子集
                for (AuthRes childAuthRes : authResList) {
                    if (childAuthRes.getParentId().equals(authRes.getId())) {
                        authResVo.addChild(childAuthRes);
                    }
                }
                //end
            }
        }
        //end
        return menuAuthResList;
    }

}
