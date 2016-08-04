package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.DeployLog;
import com.yuntao.zhushou.model.vo.DeployLogVo;
import org.apache.ibatis.annotations.Param;

public interface DeployLogMapper extends BaseMapper<DeployLog> {


    DeployLogVo findDetailById(@Param("id") Long id);


}
