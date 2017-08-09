package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.AtProcessInst;
import com.yuntao.zhushou.model.query.AtProcessInstQuery;
import com.yuntao.zhushou.model.vo.AtProcessInstVo;
import com.yuntao.zhushou.common.web.Pagination;
import java.util.List;



/**
 * 流程实例服务接口
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
public interface AtProcessInstService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<AtProcessInst> selectList(AtProcessInstQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<AtProcessInstVo> selectPage(AtProcessInstQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    AtProcessInst findById(Long id);

    /**
     * 新增
     * @param atProcessInst
     * @return
     */
    int insert(AtProcessInst atProcessInst) ;

    /**
     * 根据id修改
     * @param atProcessInst
     * @return
     */
    int updateById(AtProcessInst atProcessInst);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);



}
