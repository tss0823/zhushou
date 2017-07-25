/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.ProxyReqFilterItem;
import com.yuntao.zhushou.model.domain.ProxyResRewrite;
import com.yuntao.zhushou.model.query.ProxyResRewriteQuery;
import com.yuntao.zhushou.model.vo.ProxyResRewriteVo;
import com.yuntao.zhushou.service.support.proxy.HttpExecuteTask;

import java.util.List;


/**
 * 代理返回重写 服务接口
 * @author admin
 *
 * @2017-07-23 15
 */
public interface ProxyResRewriteService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<ProxyResRewrite> selectList(ProxyResRewriteQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    ProxyResRewrite selectOne(ProxyResRewriteQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<ProxyResRewriteVo> selectPage(ProxyResRewriteQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    ProxyResRewrite findById(Long id);

    /**
     * 新增
     *
     * @param proxyResRewrite
     * @return
     */
    int insert(ProxyResRewrite proxyResRewrite);

    /**
     * 根据id修改
     *
     * @param proxyResRewrite
     * @return
     */
    int updateById(ProxyResRewrite proxyResRewrite);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 重写处理
     * @param httpExecuteTask
     */
    boolean rewriteProcess(HttpExecuteTask httpExecuteTask);

    /**
     * 添加重写规则
     * @param proxyResRewrite
     * @param itemList
     * @return
     */
    Long addRewrite(ProxyResRewrite proxyResRewrite, List<ProxyReqFilterItem> itemList);


    

}

