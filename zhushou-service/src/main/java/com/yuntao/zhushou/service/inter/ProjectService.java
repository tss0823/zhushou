/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.Project;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.ProjectQuery;
import com.yuntao.zhushou.model.vo.ProjectVo;

import java.util.List;


/**
 * 项目 服务接口
 * @author admin
 *
 * @2018-04-05 08
 */
public interface ProjectService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<Project> selectList(ProjectQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    Project selectOne(ProjectQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<ProjectVo> selectPage(ProjectQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    Project findById(Long id);

    /**
     * 新增
     *
     * @param project
     * @return
     */
    int insert(Project project);

    /**
     * 根据id修改
     *
     * @param project
     * @return
     */
    int updateById(Project project);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 构建项目
     * @param id
     * @return
     */
    Long build(Long id,User user);

    Project getFirst(Long companyId);


    

}

