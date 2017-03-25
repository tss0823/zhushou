/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.AppVersion;
import com.yuntao.zhushou.model.query.AppVersionQuery;
import com.yuntao.zhushou.model.vo.AppVersionVo;

import java.util.List;


/**
 * 应用版本 服务接口
 * @author admin
 *
 * @2017-03-18 16
 */
public interface AppVersionService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<AppVersion> selectList(AppVersionQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    AppVersion selectOne(AppVersionQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<AppVersionVo> selectPage(AppVersionQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    AppVersion findById(Long id);

    /**
     * 新增
     *
     * @param appVersion
     * @return
     */
    int insert(AppVersion appVersion);

    /**
     * 根据id修改
     *
     * @param appVersion
     * @return
     */
    int updateById(AppVersion appVersion);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


    /**
     * 获取最新版本
     * @return
     */
    AppVersion getLastVersion(Long companyId,String appName,String model);

    /**
     * 获取准备发布的最新版本
     * @return
     */
    String getDeployVersion(Long companyId,String appName,String model);

    /**
     * 获取准备发布的最新版本
     * @return
     */
    String getDeployVersion(String lastVersion);
    

}

