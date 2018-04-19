/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.ProjectMapper;
import com.yuntao.zhushou.model.domain.Entity;
import com.yuntao.zhushou.model.domain.Project;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.EntityQuery;
import com.yuntao.zhushou.model.query.ProjectQuery;
import com.yuntao.zhushou.model.vo.ProjectVo;
import com.yuntao.zhushou.service.inter.CodeBuildService;
import com.yuntao.zhushou.service.inter.EntityService;
import com.yuntao.zhushou.service.inter.ProjectService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ProjectServiceImpl extends AbstService implements ProjectService {


    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private CodeBuildService codeBuildService;

    @Autowired
    private EntityService entityService;

    @Override
    public List<Project> selectList(ProjectQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return projectMapper.selectList(queryMap);
    }

    @Override
    public Project selectOne(ProjectQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<Project> projects = projectMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(projects)) {
            return projects.get(0);
        }
        return null;
    }

    @Override
    public Pagination<ProjectVo> selectPage(ProjectQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = projectMapper.selectListCount(queryMap);
        Pagination<ProjectVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<Project> dataList = projectMapper.selectList(queryMap);
        List<ProjectVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (Project project : dataList) {
            ProjectVo projectVo = BeanUtils.beanCopy(project, ProjectVo.class);
            newDataList.add(projectVo);
        }
        return pagination;
    }

    @Override
    public Project findById(Long id) {
        return projectMapper.findById(id);
    }


    @Override
    public int insert(Project project) {
        return projectMapper.insert(project);
    }

    @Override
    public int updateById(Project project) {
        return projectMapper.updateById(project);
    }

    @Override
    public int deleteById(Long id) {
        return projectMapper.deleteById(id);
    }

    @Override
    public Long build(Long id,User user) {
        EntityQuery entityQuery = new EntityQuery();
        List<Entity> entityList = entityService.selectList(entityQuery);
        List<Long> entityIds = new ArrayList<>();
        for (Entity entity : entityList) {
            entityIds.add(entity.getId());
        }
        Long buildRecordId = codeBuildService.buildApp(false,user, id, entityIds);
        return buildRecordId;
    }

    @Override
    public Project getFirst(Long companyId) {
        ProjectQuery projectQuery = new ProjectQuery();
        projectQuery.setCompanyId(companyId);
        Project project = this.selectOne(projectQuery);
        return project;
    }


}