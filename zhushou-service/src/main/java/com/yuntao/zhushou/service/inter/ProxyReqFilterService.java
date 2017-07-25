/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.domain.ProxyReqFilter;
import com.yuntao.zhushou.model.domain.ProxyReqFilterItem;
import com.yuntao.zhushou.model.query.ProxyReqFilterQuery;
import com.yuntao.zhushou.model.vo.ProxyReqFilterVo;

import java.util.List;


/**
 * 代理请求过滤 服务接口
 * @author admin
 *
 * @2017-07-23 15
 */
public interface ProxyReqFilterService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<ProxyReqFilter> selectList(ProxyReqFilterQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    ProxyReqFilter selectOne(ProxyReqFilterQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<ProxyReqFilterVo> selectPage(ProxyReqFilterQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    ProxyReqFilter findById(Long id);

    /**
     * 新增
     *
     * @param proxyReqFilter
     * @return
     */
    int insert(ProxyReqFilter proxyReqFilter);

    /**
     * 根据id修改
     *
     * @param proxyReqFilter
     * @return
     */
    int updateById(ProxyReqFilter proxyReqFilter);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 是否匹配，匹配才存储
     * @param proxyContent
     * @return
     */
    boolean match(ProxyContent proxyContent);

    /**
     * 检测子项是否通过过滤匹配
     * @param proxyContent
     * @param joinFlag
     * @param proxyReqFilterItemList
     * @return
     */
    boolean checkChildItemFilter(ProxyContent proxyContent, boolean joinFlag, List<ProxyReqFilterItem> proxyReqFilterItemList) ;


    

}

