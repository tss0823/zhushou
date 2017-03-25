/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.AppDownloadRecords;
import com.yuntao.zhushou.model.query.AppDownloadRecordsQuery;
import com.yuntao.zhushou.model.vo.AppDownloadRecordsVo;

import java.util.List;


/**
 * 应用下载记录 服务接口
 * @author admin
 *
 * @2017-03-18 16
 */
public interface AppDownloadRecordsService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<AppDownloadRecords> selectList(AppDownloadRecordsQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    AppDownloadRecords selectOne(AppDownloadRecordsQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<AppDownloadRecordsVo> selectPage(AppDownloadRecordsQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    AppDownloadRecords findById(Long id);

    /**
     * 新增
     *
     * @param appDownloadRecords
     * @return
     */
    int insert(AppDownloadRecords appDownloadRecords);

    /**
     * 根据id修改
     *
     * @param appDownloadRecords
     * @return
     */
    int updateById(AppDownloadRecords appDownloadRecords);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


    

}

