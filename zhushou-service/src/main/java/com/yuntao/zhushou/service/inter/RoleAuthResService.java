package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.RoleAuthRes;
import com.yuntao.zhushou.model.query.RoleAuthResQuery;
import com.yuntao.zhushou.model.vo.RoleAuthResVo;
import com.yuntao.zhushou.model.web.Pagination;
import java.util.List;



/**
 * 角色权限资源服务接口
 * 
 * @author admin
 *
 * @2016-08-07 11
 */
public interface RoleAuthResService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<RoleAuthRes> selectList(RoleAuthResQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<RoleAuthResVo> selectPage(RoleAuthResQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    RoleAuthRes findById(Long id);

    /**
     * 新增
     * @param roleAuthRes
     * @return
     */
    int insert(RoleAuthRes roleAuthRes) ;

    /**
     * 根据id修改
     * @param roleAuthRes
     * @return
     */
    int updateById(RoleAuthRes roleAuthRes);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);




}
