/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.AtVariable;
import com.yuntao.zhushou.model.query.AtVariableQuery;
import com.yuntao.zhushou.model.vo.AtVariableVo;

import java.util.List;
import java.util.Map;


/**
 * 测试变量 服务接口
 * @author admin
 *
 * @2017-07-31 16
 */
public interface AtVariableService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<AtVariable> selectList(AtVariableQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    AtVariable selectOne(AtVariableQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<AtVariableVo> selectPage(AtVariableQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    AtVariable findById(Long id);

    /**
     * 新增
     *
     * @param atVariable
     * @return
     */
    int insert(AtVariable atVariable);

    /**
     * 根据id修改
     *
     * @param atVariable
     * @return
     */
    int updateById(AtVariable atVariable);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 获取全局变量
     * @return
     */
    List<AtVariable> getGlobalList();

    /**
     * 获取私有变量
     * @param templateId
     * @return
     */
    List<AtVariable> getPriList(Long templateId);

    /**
     * 获取可用变量，私有优先替换
     * @param templateId
     * @return
     */
    Map<String,Object> getVariableMap(Long templateId) ;

    

}

