/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.CodeBuildSql;
import com.yuntao.zhushou.model.query.CodeBuildSqlQuery;
import com.yuntao.zhushou.model.vo.CodeBuildSqlVo;

import java.util.List;


/**
 * 代码生成sql 服务接口
 * @author admin
 *
 * @2017-09-14 17
 */
public interface CodeBuildSqlService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<CodeBuildSql> selectList(CodeBuildSqlQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    CodeBuildSql selectOne(CodeBuildSqlQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<CodeBuildSqlVo> selectPage(CodeBuildSqlQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    CodeBuildSql findById(Long id);

    /**
     * 新增
     *
     * @param codeBuildSql
     * @return
     */
    int insert(CodeBuildSql codeBuildSql);

    /**
     * 根据id修改
     *
     * @param codeBuildSql
     * @return
     */
    int updateById(CodeBuildSql codeBuildSql);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


    

}

