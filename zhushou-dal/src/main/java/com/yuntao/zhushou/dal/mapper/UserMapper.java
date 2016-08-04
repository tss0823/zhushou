package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

    int updateStatus(@Param("id") Long id,
                     @Param("oldStatus") Integer oldstatus,
                     @Param("newStatus") Integer newStatus);

}
