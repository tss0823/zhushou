package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.query.ProxyContentQuery;
import com.yuntao.zhushou.model.vo.ProxyContentVo;
import com.yuntao.zhushou.model.web.Pagination;

import java.util.List;


/**
 * 代理内容服务接口
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
public interface ProxyContentService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<ProxyContent> selectList(ProxyContentQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<ProxyContentVo> selectPage(ProxyContentQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    ProxyContent findById(Long id);

    /**
     * 新增
     * @param reqContent
     * @return
     */
    int insert(ProxyContent reqContent) ;

    /**
     * 根据id修改
     * @param reqContent
     * @return
     */
    int updateById(ProxyContent reqContent);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);



}
