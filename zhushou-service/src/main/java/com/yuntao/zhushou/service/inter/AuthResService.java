package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.AuthRes;
import com.yuntao.zhushou.model.query.AuthResQuery;
import com.yuntao.zhushou.model.vo.AuthResVo;
import com.yuntao.zhushou.common.web.Pagination;
import java.util.List;



/**
 * 权限资源服务接口
 * 
 * @author admin
 *
 * @2016-08-07 11
 */
public interface AuthResService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<AuthRes> selectList(AuthResQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<AuthResVo> selectPage(AuthResQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    AuthRes findById(Long id);

    /**
     * 新增
     * @param authRes
     * @return
     */
    int insert(AuthRes authRes) ;

    /**
     * 根据id修改
     * @param authRes
     * @return
     */
    int updateById(AuthRes authRes);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    List<AuthResVo> selectByUserId(Long userId);


}
