package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.dal.mapper.ConfigMapper;
import com.yuntao.zhushou.model.domain.Config;
import com.yuntao.zhushou.model.enums.ConfigType;
import com.yuntao.zhushou.model.query.ConfigQuery;
import com.yuntao.zhushou.service.inter.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shengshan.tang on 2015/12/12 at 16:02
 */
@Service
public class ConfigServiceImpl extends AbstService implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Override
    public String getValueByName(Long companyId,String name) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("companyId", companyId);
        queryMap.put("name", name);
        return configMapper.findByCondition(queryMap).getValue();
    }

    @Override
    public Config getByName(Long companyId,String name) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("companyId", companyId);
        queryMap.put("name", name);
        return configMapper.findByCondition(queryMap);
    }

    @Override
    public List<Config> selectList() {
        return configMapper.selectList(new HashMap());
    }

    @Override
    public List<Config> selectList(ConfigQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return configMapper.selectList(queryMap);
    }

    @Override
    public List<Config> selectPubList() {
        ConfigQuery configQuery = new ConfigQuery();
        configQuery.setType(ConfigType.company.getCode());
        Map<String, Object> queryMap = BeanUtils.beanToMap(configQuery);
        return configMapper.selectList(queryMap);
    }

    @Override
    public List<Config> selectPriList(Long companyId) {
        ConfigQuery configQuery = new ConfigQuery();
        configQuery.setType(ConfigType.system.getCode());
        configQuery.setCompanyId(companyId);
        Map<String, Object> queryMap = BeanUtils.beanToMap(configQuery);
        return configMapper.selectList(queryMap);
    }

    @Override
    public List<Config> selectProjectList(Long projectId) {
        ConfigQuery configQuery = new ConfigQuery();
        configQuery.setType(ConfigType.project.getCode());
        configQuery.setProjectId(projectId);
        return this.selectList(configQuery);
    }

    @Transactional
    @Override
    public int saveCompanyConfig(Long companyId, List<Config> dataList) {
        //del old
        configMapper.delByCompanyId(companyId);
        for (Config config : dataList) {
            config.setType(ConfigType.company.getCode());
            config.setCompanyId(companyId);
            this.configMapper.insert(config);
        }
        return 1;
    }

    @Transactional
    @Override
    public int saveProjectConfig(Long projectId, List<Config> dataList) {
        //del old
        configMapper.delByProjectId(projectId);
        for (Config config : dataList) {
            config.setType(ConfigType.project.getCode());
            config.setProjectId(projectId);
            this.configMapper.insert(config);
        }
        return 1;
    }
}
