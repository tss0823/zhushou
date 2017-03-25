package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.AppFront;
import org.apache.ibatis.annotations.Param;

public interface AppFrontMapper extends BaseMapper<AppFront> {

    int updateLog(@Param("companyId") Long companyId, @Param("name") String appName, @Param("log") String log);


}
