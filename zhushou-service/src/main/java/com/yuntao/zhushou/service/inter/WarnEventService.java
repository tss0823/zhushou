/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.WarnEvent;
import com.yuntao.zhushou.model.domain.WarnEventResult;
import com.yuntao.zhushou.model.query.WarnEventQuery;
import com.yuntao.zhushou.model.vo.WarnEventVo;

import java.util.List;


/**
 * 告警事件 服务接口
 * @author admin
 *
 * @2017-06-05 00
 */
public interface WarnEventService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<WarnEvent> selectList(WarnEventQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    WarnEvent selectOne(WarnEventQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<WarnEventVo> selectPage(WarnEventQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    WarnEvent findById(Long id);

    /**
     * 新增
     *
     * @param warnEvent
     * @return
     */
    int insert(WarnEvent warnEvent);

    /**
     * 根据id修改
     *
     * @param warnEvent
     * @return
     */
    int updateById(WarnEvent warnEvent);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

    void sendTask(List<WarnEvent> warnEventList);

    void tryErrorSendTask(List<WarnEventResult> warnEventResultList);


    

}

