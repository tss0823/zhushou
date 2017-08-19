package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.AtParameter;
import org.apache.ibatis.annotations.Param;

/**
 * 模板参数Mappper
 * 
 * @author admin
 *
 * @2016-07-21 16
 */
public interface AtParameterMapper extends BaseMapper<AtParameter> {

    int deleteByActiveId(@Param("activeId") Long activeId);


}
