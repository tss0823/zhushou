package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.dal.mapper.ConfigMapper;
import com.yuntao.zhushou.model.domain.Config;
import com.yuntao.zhushou.model.enums.ConfigType;
import com.yuntao.zhushou.model.query.ConfigQuery;
import com.yuntao.zhushou.service.inter.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String getValueByName(String name) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("name", name);
        return configMapper.findByCondition(queryMap).getValue();
    }

    @Override
    public int getIntByName(String name) {
        return Integer.valueOf(getValueByName(name));
    }

    @Override
    public Config getByName(String name) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("name", name);
        return configMapper.findByCondition(queryMap);
    }

    @Override
    public List<Config> selectList() {
        return configMapper.selectList(new HashMap());
    }

    @Override
    public List<Config> selectPubList() {
        ConfigQuery configQuery = new ConfigQuery();
        configQuery.setType(ConfigType.pub.getCode());
        Map<String, Object> queryMap = BeanUtils.beanToMap(configQuery);
        return configMapper.selectList(queryMap);
    }

    @Override
    public List<Config> selectPriList(Long companyId) {
        ConfigQuery configQuery = new ConfigQuery();
        configQuery.setType(ConfigType.pri.getCode());
        configQuery.setCompanyId(companyId);
        Map<String, Object> queryMap = BeanUtils.beanToMap(configQuery);
        return configMapper.selectList(queryMap);
    }
}
