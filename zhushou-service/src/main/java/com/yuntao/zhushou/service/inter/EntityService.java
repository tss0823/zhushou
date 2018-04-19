/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.Entity;
import com.yuntao.zhushou.model.query.EntityQuery;
import com.yuntao.zhushou.model.vo.EntityVo;

import java.util.List;


/**
 * 实体 服务接口
 * @author admin
 *
 * @2018-04-05 08
 */
public interface EntityService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<Entity> selectList(EntityQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    Entity selectOne(EntityQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<EntityVo> selectPage(EntityQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    Entity findById(Long id);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    int insert(Entity entity);

    /**
     * 根据id修改
     *
     * @param entity
     * @return
     */
    int updateById(Entity entity);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


    

}

