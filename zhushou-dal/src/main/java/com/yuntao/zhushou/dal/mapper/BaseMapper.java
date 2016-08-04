package com.yuntao.zhushou.dal.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by shengshan.tang on 2015/12/12 at 16:08
 */
public interface BaseMapper<T> {

    T findById(@Param(value = "id") Long id);

    T findByCondition(Map<String, Object> queryMap);

    List<T> selectList(Map<String, Object> queryMap);

    Long selectListCount(Map<String, Object> queryMap);

    int insert(T domain);

    int updateById(T domain);

    int deleteById(@Param(value = "id") Long id);
}
