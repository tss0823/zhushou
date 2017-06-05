/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.WarnEventResult;
import com.yuntao.zhushou.model.query.WarnEventResultQuery;
import com.yuntao.zhushou.model.vo.WarnEventResultVo;

import java.util.List;


/**
 * 告警事件发送结果 服务接口
 * @author admin
 *
 * @2017-06-05 00
 */
public interface WarnEventResultService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<WarnEventResult> selectList(WarnEventResultQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    WarnEventResult selectOne(WarnEventResultQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<WarnEventResultVo> selectPage(WarnEventResultQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    WarnEventResult findById(Long id);

    /**
     * 新增
     *
     * @param warnEventResult
     * @return
     */
    int insert(WarnEventResult warnEventResult);

    /**
     * 根据id修改
     *
     * @param warnEventResult
     * @return
     */
    int updateById(WarnEventResult warnEventResult);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


    

}

