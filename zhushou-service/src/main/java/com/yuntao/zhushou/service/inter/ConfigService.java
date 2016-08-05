package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.Config;

import java.util.List;

/**
 * Created by shengshan.tang on 2015/12/12 at 16:02
 */
public interface ConfigService {

    String getValueByName(String name);

    int getIntByName(String name);

    Config getByName(String name);

    List<Config> selectList();
}
