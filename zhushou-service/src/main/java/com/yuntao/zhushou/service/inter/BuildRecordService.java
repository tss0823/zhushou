/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.BuildRecord;
import com.yuntao.zhushou.model.query.BuildRecordQuery;
import com.yuntao.zhushou.model.vo.BuildRecordVo;

import java.util.List;


/**
 * 构建记录 服务接口
 * @author admin
 *
 * @2018-04-05 08
 */
public interface BuildRecordService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<BuildRecord> selectList(BuildRecordQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    BuildRecord selectOne(BuildRecordQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<BuildRecordVo> selectPage(BuildRecordQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    BuildRecord findById(Long id);

    /**
     * 新增
     *
     * @param buildRecord
     * @return
     */
    int insert(BuildRecord buildRecord);

    /**
     * 根据id修改
     *
     * @param buildRecord
     * @return
     */
    int updateById(BuildRecord buildRecord);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


    

}

