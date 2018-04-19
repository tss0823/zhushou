/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.Template;
import com.yuntao.zhushou.model.query.TemplateQuery;
import com.yuntao.zhushou.model.vo.TemplateVo;

import java.util.List;


/**
 * 构建模版 服务接口
 * @author admin
 *
 * @2018-04-05 08
 */
public interface TemplateService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<Template> selectList(TemplateQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    Template selectOne(TemplateQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<TemplateVo> selectPage(TemplateQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    Template findById(Long id);

    /**
     * 新增
     *
     * @param template
     * @return
     */
    int insert(Template template);

    /**
     * 根据id修改
     *
     * @param template
     * @return
     */
    int updateById(Template template);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


    

}

