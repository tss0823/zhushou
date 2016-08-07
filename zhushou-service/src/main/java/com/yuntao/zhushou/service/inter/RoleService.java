package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.Role;
import com.yuntao.zhushou.model.query.RoleQuery;
import com.yuntao.zhushou.model.vo.RoleVo;
import com.yuntao.zhushou.model.web.Pagination;
import java.util.List;



/**
 * 角色服务接口
 * 
 * @author admin
 *
 * @2016-08-07 11
 */
public interface RoleService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<Role> selectList(RoleQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<RoleVo> selectPage(RoleQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    Role findById(Long id);

    /**
     * 新增
     * @param role
     * @return
     */
    int insert(Role role) ;

    /**
     * 根据id修改
     * @param role
     * @return
     */
    int updateById(Role role);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);



}
