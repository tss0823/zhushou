package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.App;
import org.apache.ibatis.annotations.Param;

public interface AppMapper extends BaseMapper<App> {

    int updateLog(@Param("companyId") Long companyId,@Param("name") String appName,@Param("log") String log);


}
