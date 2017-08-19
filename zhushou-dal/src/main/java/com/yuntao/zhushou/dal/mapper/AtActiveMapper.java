package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.AtActive;
import org.apache.ibatis.annotations.Param;

/**
 * 活动模板Mappper
 * 
 * @author admin
 *
 * @2016-07-21 16
 */
public interface AtActiveMapper extends BaseMapper<AtActive> {

    int deleteByTemplateId(@Param("templateId") Long templateId);


}
