package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.Host;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HostMapper extends BaseMapper<Host> {

    List<Host> selectListByAppAndModel(@Param("appId") Long appId, @Param("model") String model);

    List<Host> selectListByAppId(@Param("appId") Long appId);


}
