package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.AuthRes;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 权限资源Mappper
 * 
 * @author admin
 *
 * @2016-08-07 11
 */
public interface AuthResMapper extends BaseMapper<AuthRes> {

    List<AuthRes> selectByRole(@Param("roleIds") List<Long> roleIds);


}
