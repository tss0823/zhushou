package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.ShowRes;
import com.yuntao.zhushou.model.query.ShowResQuery;
import com.yuntao.zhushou.model.vo.ShowResVo;
import com.yuntao.zhushou.common.web.Pagination;

import java.util.List;


/**
 * 展示资源服务接口
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
public interface ShowResService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<ShowRes> selectList(ShowResQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<ShowResVo> selectPage(ShowResQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    ShowRes findById(Long id);

    /**
     * 新增
     * @param showRes
     * @return
     */
    int insert(ShowRes showRes) ;

    /**
     * 根据id修改
     * @param showRes
     * @return
     */
    int updateById(ShowRes showRes);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);



}
