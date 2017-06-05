package com.yuntao.zhushou.dal.mapper;



import com.yuntao.zhushou.model.domain.WarnEvent;
import org.apache.ibatis.annotations.Param;


/**
 * 告警事件Mapper
 * @author admin
 *
 * @2017-06-05 00
 */
public interface WarnEventMapper extends BaseMapper<WarnEvent> {

    int updateStatusById(@Param(value = "id") Long id, @Param(value = "status") Integer status);


}
