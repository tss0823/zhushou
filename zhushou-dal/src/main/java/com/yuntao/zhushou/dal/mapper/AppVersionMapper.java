package com.yuntao.zhushou.dal.mapper;


import com.yuntao.zhushou.model.domain.AppVersion;

import java.util.Map;


/**
 * 应用版本Mapper
 * @author admin
 *
 * @2017-03-18 16
 */
public interface AppVersionMapper extends BaseMapper<AppVersion> {


    AppVersion getLastVersion(Map<String,Object> queryMap);
}
