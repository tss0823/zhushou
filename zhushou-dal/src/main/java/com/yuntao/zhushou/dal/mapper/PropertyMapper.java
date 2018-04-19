package com.yuntao.zhushou.dal.mapper;


import com.yuntao.zhushou.model.domain.Property;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 属性Mapper
 * @author admin
 *
 * @2018-04-05 08
 */
public interface PropertyMapper extends BaseMapper<Property> {


    int insertBatch(@Param("list") List<Property> dataList);

    int deleteByEntityId(@Param("entityId") Long entityId);
}
