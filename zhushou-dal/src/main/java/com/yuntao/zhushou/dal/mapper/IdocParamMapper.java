package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.IdocParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 接口请求参数Mappper
 * 
 * @author admin
 *
 * @2016-07-30 20
 */
public interface IdocParamMapper extends BaseMapper<IdocParam> {

    int deleteByParentId(@Param("id") Long id);

}
