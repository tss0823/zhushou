package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.AtTemplate;
import com.yuntao.zhushou.model.query.AtTemplateQuery;
import com.yuntao.zhushou.model.vo.AtTemplateVo;
import com.yuntao.zhushou.common.web.Pagination;
import java.util.List;



/**
 * 测试模板服务接口
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
public interface AtTemplateService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<AtTemplate> selectList(AtTemplateQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<AtTemplateVo> selectPage(AtTemplateQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    AtTemplate findById(Long id);

    /**
     * 新增
     * @param atTemplate
     * @return
     */
    int insert(AtTemplate atTemplate) ;

    /**
     * 根据id修改
     * @param atTemplate
     * @return
     */
    int updateById(AtTemplate atTemplate);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 保存模板
     * @param logIds
     * @param templateVo
     * @return
     */
    void save(AtTemplateVo templateVo, List<String> logIds);

    /**
     * 获取模板对象
     * @param templteId
     * @return
     */
    AtTemplateVo getTemplateVo(Long templteId);



}
