package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.cache.CacheService;
import com.yuntao.zhushou.common.constant.CacheConstant;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.AppMapper;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.enums.YesNoIntType;
import com.yuntao.zhushou.model.query.AppQuery;
import com.yuntao.zhushou.model.vo.AppVo;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.AppVersionService;
import com.yuntao.zhushou.service.inter.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shan on 2016/3/27.
 */
@Service
public class AppServiceImpl extends AbstService implements AppService {

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AppVersionService appVersionService;

    @Override
    public App findById(Long id) {
        return appMapper.findById(id);
    }

    @Override
    public App findByName(Long companyId, String name) {
        AppQuery query = new AppQuery();
        query.setCompanyId(companyId);
        query.setName(name);
        Map<String,Object> queryMap = BeanUtils.beanToMap(query);
        return appMapper.findByCondition(queryMap);
    }

    @Override
    public int updateLog(Long companyId,String appName,String log) {
        return appMapper.updateLog(companyId,appName,log);
    }

    @Override
    public Pagination<AppVo> selectPage(AppQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = appMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return new Pagination<>();
        }
        Pagination<App> pageInfo = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        List<App> dataList = appMapper.selectList(queryMap);
        List<AppVo> resultDataList = new ArrayList();
        Pagination<AppVo> newPageInfo = new Pagination<>(pageInfo);
        newPageInfo.setDataList(resultDataList);
        for(App app : dataList){
            AppVo appVo = BeanUtils.beanCopy(app,AppVo.class);
            resultDataList.add(appVo);
            if(appVo.getUserId() != null){
                User user = userService.findById(appVo.getUserId());
                if(user != null){
                    appVo.setUserName(user.getNickName());
                }
            }
            String lastTime = DateUtil.getRangeTime(appVo.getGmtModify(),"yyyy-MM-dd HH:mm:ss");
            appVo.setLastTime(lastTime);

        }
        return newPageInfo;
    }

    @Override
    public Pagination<AppVo> selectFrontPage(AppQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = appMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return new Pagination<>();
        }
        Pagination<App> pageInfo = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        List<App> dataList = appMapper.selectList(queryMap);
        List<AppVo> resultDataList = new ArrayList();
        Pagination<AppVo> newPageInfo = new Pagination<>(pageInfo);
        newPageInfo.setDataList(resultDataList);
        for(App app : dataList){
            AppVo appVo = BeanUtils.beanCopy(app,AppVo.class);
            resultDataList.add(appVo);
            if(appVo.getUserId() != null){
                User user = userService.findById(appVo.getUserId());
                if(user != null){
                    appVo.setUserName(user.getNickName());
                }
            }
            String lastTime = DateUtil.getRangeTime(appVo.getGmtModify(),"yyyy-MM-dd HH:mm:ss");
            appVo.setLastTime(lastTime);

//            //appVersion
//            AppVersion testAppVersion = appVersionService.getLastVersion(query.getCompanyId(),appVo.getName(),"test");
//            appVo.setTestVersion(testAppVersion.getVersion());
//            String testDeployVersion = appVersionService.getDeployVersion(testAppVersion.getVersion());
//            appVo.setTestDeployVersion(testDeployVersion);
//
//            AppVersion prodAppVersion = appVersionService.getLastVersion(query.getCompanyId(),appVo.getName(),"prod");
//            appVo.setProdVersion(prodAppVersion.getVersion());
//            String prodDeployVersion = appVersionService.getDeployVersion(prodAppVersion.getVersion());
//            appVo.setProdDeployVersion(prodDeployVersion);


        }
        return newPageInfo;
    }

    @Override
    public List<App> selectAllList() {
        //get form cache
        String key = CacheConstant.App.selectAllList;
        List<App> dataList = (List<App>) cacheService.get(key);
        if(CollectionUtils.isNotEmpty(dataList)){
            return dataList;
        }
        dataList = appMapper.selectList(new HashMap<String, Object>());

        //set to cache
        cacheService.set(key,dataList);
        return dataList;

    }

    @Override
    public List<App> selectList(AppQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return appMapper.selectList(queryMap);
    }

    @Override
    public List<App> selectByCompanyId(Long companyId) {
        AppQuery appQuery = new AppQuery();
        appQuery.setCompanyId(companyId);
        return selectList(appQuery);
    }

    @Override
    public int insert(App app) {
        app.setType(YesNoIntType.yes.getCode());
        app.setStatus(YesNoIntType.yes.getCode());
        return appMapper.insert(app);
    }

    @Override
    public int updateById(App app) {
        return appMapper.updateById(app);
    }

    @Override
    public int deleteById(Long id) {
        return appMapper.deleteById(id);
    }
}
