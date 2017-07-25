/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.ProxyReqFilterItem;
import com.yuntao.zhushou.model.query.ProxyReqFilterItemQuery;
import com.yuntao.zhushou.model.vo.ProxyReqFilterItemVo;

import java.util.List;


/**
 * 代理请求过滤子集 服务接口
 * @author admin
 *
 * @2017-07-23 15
 */
public interface ProxyReqFilterItemService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<ProxyReqFilterItem> selectList(ProxyReqFilterItemQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    ProxyReqFilterItem selectOne(ProxyReqFilterItemQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<ProxyReqFilterItemVo> selectPage(ProxyReqFilterItemQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    ProxyReqFilterItem findById(Long id);

    /**
     * 新增
     *
     * @param proxyReqFilterItem
     * @return
     */
    int insert(ProxyReqFilterItem proxyReqFilterItem);

    /**
     * 根据id修改
     *
     * @param proxyReqFilterItem
     * @return
     */
    int updateById(ProxyReqFilterItem proxyReqFilterItem);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 查询列表
     *
     * @param filterType (0 req,1 rewrite)
     * @param parentId
     * @return
     */
    List<ProxyReqFilterItem> selectByParentId(Integer filterType,Long parentId);


    

}

