package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.AtActive;
import com.yuntao.zhushou.model.query.AtActiveQuery;
import com.yuntao.zhushou.model.vo.AtActiveVo;
import com.yuntao.zhushou.common.web.Pagination;
import java.util.List;



/**
 * 活动模板服务接口
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
public interface AtActiveService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<AtActive> selectList(AtActiveQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<AtActiveVo> selectPage(AtActiveQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    AtActive findById(Long id);

    /**
     * 新增
     * @param atActive
     * @return
     */
    int insert(AtActive atActive) ;

    /**
     * 根据id修改
     * @param atActive
     * @return
     */
    int updateById(AtActive atActive);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);



}
