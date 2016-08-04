package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.AtActiveInst;
import com.yuntao.zhushou.model.query.AtActiveInstQuery;
import com.yuntao.zhushou.model.vo.AtActiveInstVo;
import com.yuntao.zhushou.model.web.Pagination;
import java.util.List;



/**
 * 活动实例服务接口
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
public interface AtActiveInstService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<AtActiveInst> selectList(AtActiveInstQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<AtActiveInstVo> selectPage(AtActiveInstQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    AtActiveInst findById(Long id);

    /**
     * 新增
     * @param atActiveInst
     * @return
     */
    int insert(AtActiveInst atActiveInst) ;

    /**
     * 根据id修改
     * @param atActiveInst
     * @return
     */
    int updateById(AtActiveInst atActiveInst);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);



}
