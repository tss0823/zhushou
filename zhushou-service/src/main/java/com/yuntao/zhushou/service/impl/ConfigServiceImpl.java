package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.dal.mapper.ConfigMapper;
import com.yuntao.zhushou.model.domain.Config;
import com.yuntao.zhushou.service.inter.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
}
