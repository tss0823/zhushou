package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.cache.CacheService;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.AppFrontMapper;
import com.yuntao.zhushou.model.domain.AppFront;
import com.yuntao.zhushou.model.domain.AppVersion;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.AppFrontQuery;
import com.yuntao.zhushou.model.vo.AppFrontVo;
import com.yuntao.zhushou.service.inter.AppFrontService;
import com.yuntao.zhushou.service.inter.AppVersionService;
import com.yuntao.zhushou.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shan on 2016/3/27.
 */
@Service
public class AppFrontServiceImpl extends AbstService implements AppFrontService {

    @Autowired
    private AppFrontMapper appFrontMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AppVersionService appVersionService;

    @Override
    public AppFront findById(Long id) {
        return appFrontMapper.findById(id);
    }

    @Override
    public AppFront findByName(Long companyId, String name) {
        AppFrontQuery query = new AppFrontQuery();
        query.setCompanyId(companyId);
        query.setName(name);
        Map<String,Object> queryMap = BeanUtils.beanToMap(query);
        return appFrontMapper.findByCondition(queryMap);
    }

    @Override
    public int updateLog(Long companyId,String appFrontName,String log) {
        return appFrontMapper.updateLog(companyId,appFrontName,log);
    }

    @Override
    public Pagination<AppFrontVo> selectPage(AppFrontQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = appFrontMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<AppFront> pageInfo = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        List<AppFront> dataList = appFrontMapper.selectList(queryMap);
        List<AppFrontVo> resultDataList = new ArrayList();
        Pagination<AppFrontVo> newPageInfo = new Pagination<>(pageInfo);
        newPageInfo.setDataList(resultDataList);
        for(AppFront appFront : dataList){
            AppFrontVo appFrontVo = BeanUtils.beanCopy(appFront,AppFrontVo.class);
            resultDataList.add(appFrontVo);
            if(appFrontVo.getUserId() != null){
                User user = userService.findById(appFrontVo.getUserId());
                if(user != null){
                    appFrontVo.setUserName(user.getNickName());
                }
            }
            String lastTime = DateUtil.getRangeTime(appFrontVo.getGmtModify(),"yyyy-MM-dd HH:mm:ss");
            appFrontVo.setLastTime(lastTime);

        }
        return newPageInfo;
    }

    @Override
    public Pagination<AppFrontVo> selectFrontPage(AppFrontQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = appFrontMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<AppFront> pageInfo = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        List<AppFront> dataList = appFrontMapper.selectList(queryMap);
        List<AppFrontVo> resultDataList = new ArrayList();
        Pagination<AppFrontVo> newPageInfo = new Pagination<>(pageInfo);
        newPageInfo.setDataList(resultDataList);
        for(AppFront appFront : dataList){
            AppFrontVo appFrontVo = BeanUtils.beanCopy(appFront,AppFrontVo.class);
            resultDataList.add(appFrontVo);
            if(appFrontVo.getUserId() != null){
                User user = userService.findById(appFrontVo.getUserId());
                if(user != null){
                    appFrontVo.setUserName(user.getNickName());
                }
            }
            String lastTime = DateUtil.getRangeTime(appFrontVo.getGmtModify(),"yyyy-MM-dd HH:mm:ss");
            appFrontVo.setLastTime(lastTime);

            //appFrontVersion
            AppVersion testAppFrontVersion = appVersionService.getLastVersion(query.getCompanyId(),appFrontVo.getName(),"test");
            appFrontVo.setTestVersion(testAppFrontVersion.getVersion());
            String testDeployVersion = appVersionService.getDeployVersion(testAppFrontVersion.getVersion());
            appFrontVo.setTestDeployVersion(testDeployVersion);

            AppVersion prodAppFrontVersion = appVersionService.getLastVersion(query.getCompanyId(),appFrontVo.getName(),"prod");
            appFrontVo.setProdVersion(prodAppFrontVersion.getVersion());
            String prodDeployVersion = appVersionService.getDeployVersion(prodAppFrontVersion.getVersion());
            appFrontVo.setProdDeployVersion(prodDeployVersion);


        }
        return newPageInfo;
    }


    @Override
    public List<AppFront> selectList(AppFrontQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return appFrontMapper.selectList(queryMap);
    }

    @Override
    public List<AppFront> selectByCompanyId(Long companyId) {
        AppFrontQuery appFrontQuery = new AppFrontQuery();
        appFrontQuery.setCompanyId(companyId);
        return selectList(appFrontQuery);
    }
}
