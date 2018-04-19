/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.Property;
import com.yuntao.zhushou.model.query.PropertyQuery;
import com.yuntao.zhushou.model.vo.PropertyVo;

import java.util.List;


/**
 * 属性 服务接口
 * @author admin
 *
 * @2018-04-05 08
 */
public interface PropertyService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<Property> selectList(PropertyQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    Property selectOne(PropertyQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<PropertyVo> selectPage(PropertyQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    Property findById(Long id);

    /**
     * 新增
     *
     * @param property
     * @return
     */
    int insert(Property property);

    /**
     * 根据id修改
     *
     * @param property
     * @return
     */
    int updateById(Property property);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

    int insertBatch(Long entityId,List<Property> dataList);


    

}

