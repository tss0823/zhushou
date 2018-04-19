package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.Config;
import com.yuntao.zhushou.model.query.ConfigQuery;

import java.util.List;

/**
 * Created by shengshan.tang on 2015/12/12 at 16:02
 */
public interface ConfigService {

    String getValueByName(Long companyId,String name);

    Config getByName(Long companyId,String name);

    List<Config> selectList();

    List<Config> selectList(ConfigQuery query);

    List<Config> selectPubList();

    List<Config> selectPriList(Long companyId);

    List<Config> selectProjectList(Long projectId);

    int saveCompanyConfig(Long companyId,List<Config> dataList);

    int saveProjectConfig(Long projectId,List<Config> dataList);

}
