package com.yuntao.zhushou.dal.mapper;


import com.yuntao.zhushou.model.domain.Config;
import org.apache.ibatis.annotations.Param;

/**
 * config Mappper
 */
public interface ConfigMapper extends BaseMapper<Config> {

    int delByCompanyId(@Param("companyId") Long companyId);

    int delByProjectId(@Param("projectId") Long projectId);

}
