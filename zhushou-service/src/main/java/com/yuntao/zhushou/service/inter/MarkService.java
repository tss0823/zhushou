/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.Mark;
import com.yuntao.zhushou.model.query.MarkQuery;
import com.yuntao.zhushou.model.vo.MarkVo;
import com.yuntao.zhushou.model.vo.mark.MarkTopNVo;

import java.util.List;


/**
 * 马克 服务接口
 * @author admin
 *
 * @2017-08-31 15
 */
public interface MarkService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<Mark> selectList(MarkQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    Mark selectOne(MarkQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<MarkVo> selectPage(MarkQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    Mark findById(Long id);

    /**
     * 新增
     *
     * @param mark
     * @return
     */
    int insert(Mark mark);

    /**
     * 根据id修改
     *
     * @param mark
     * @return
     */
    int updateById(Mark mark);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


    /**
     * topN
     * @param type 类型
     * @param startPeriods 开始期数
     * @param endPeriods 结束期数
     * @param ascOrDesc 最小，最大
     * @param topN
     * @return
     */
    List<MarkTopNVo> selectTopN(Integer type, Integer startPeriods,Integer endPeriods,String ascOrDesc,Integer topN);

    /**
     * 获取最后一个位置
     * @param val
     * @return
     */
    int selectLastLocation(Integer endPeriods,Integer val);

    /**
     * 获取最后数量
     * @return
     */
    int selectLastItemIndex(Integer endPeriods);
    

}

