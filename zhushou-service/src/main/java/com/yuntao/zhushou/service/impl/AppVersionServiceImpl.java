/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.VersionUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.AppVersionMapper;
import com.yuntao.zhushou.model.domain.AppVersion;
import com.yuntao.zhushou.model.enums.AppVerionStatus;
import com.yuntao.zhushou.model.query.AppVersionQuery;
import com.yuntao.zhushou.model.vo.AppVersionVo;
import com.yuntao.zhushou.service.inter.AppVersionService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("appVersion")
public class AppVersionServiceImpl extends AbstService implements AppVersionService {


    @Autowired
    private AppVersionMapper appVersionMapper;

    @Override
    public List<AppVersion> selectList(AppVersionQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return appVersionMapper.selectList(queryMap);
    }

    @Override
    public AppVersion selectOne(AppVersionQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<AppVersion> appVersions = appVersionMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(appVersions)) {
            return appVersions.get(0);
        }
        return null;
    }

    @Override
    public Pagination<AppVersionVo> selectPage(AppVersionQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = appVersionMapper.selectListCount(queryMap);
        Pagination<AppVersionVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<AppVersion> dataList = appVersionMapper.selectList(queryMap);
        List<AppVersionVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (AppVersion appVersion : dataList) {
            AppVersionVo appVersionVo = BeanUtils.beanCopy(appVersion, AppVersionVo.class);
            newDataList.add(appVersionVo);
        }
        return pagination;
    }

    @Override
    public AppVersion findById(Long id) {
        return appVersionMapper.findById(id);
    }


    @Override
    public int insert(AppVersion appVersion) {
        return appVersionMapper.insert(appVersion);
    }

    @Override
    public int updateById(AppVersion appVersion) {
        return appVersionMapper.updateById(appVersion);
    }

    @Override
    public int deleteById(Long id) {
        return appVersionMapper.deleteById(id);
    }

    @Override
    public AppVersion getLastVersion(Long companyId, String appName,String type, String model) {
        AppVersionQuery appVersionQuery = new AppVersionQuery();
        appVersionQuery.setCompanyId(companyId);
        appVersionQuery.setAppName(appName);
        appVersionQuery.setType(type);
        appVersionQuery.setModel(model);
        appVersionQuery.setStatus(AppVerionStatus.online.getCode());
        Map<String, Object> queryMap = BeanUtils.beanToMap(appVersionQuery);
        AppVersion lastVersion = appVersionMapper.getLastVersion(queryMap);
        if(lastVersion == null){
            lastVersion = new AppVersion();
            lastVersion.setVersion("0.0.0");
            lastVersion.setAppName(appName);
            lastVersion.setModel(model);
        }
        return lastVersion;
    }

    @Override
    public String  getDeployVersion(Long companyId, String appName,String type, String model) {
        AppVersion lastVersion = this.getLastVersion(companyId, appName,type, model);
        String version = "0.0.1";
        if(lastVersion != null){
            version = VersionUtils.dcrVersion(version);
        }
        return version;
    }

    @Override
    public String getDeployVersion(String lastVersion) {
//        if(StringUtils.isEmpty(lastVersion)){
//            lastVersion = "0.0.1";
//        }
        return VersionUtils.dcrVersion(lastVersion);
    }


}