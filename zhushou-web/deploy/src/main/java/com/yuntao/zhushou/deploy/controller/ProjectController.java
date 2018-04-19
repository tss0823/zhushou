package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.Config;
import com.yuntao.zhushou.model.domain.Project;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.param.ProjectConfigParam;
import com.yuntao.zhushou.model.query.ProjectQuery;
import com.yuntao.zhushou.model.vo.ProjectVo;
import com.yuntao.zhushou.service.inter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("project")
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AppService appService;

    @Autowired
    private HostService hostService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigService configService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(ProjectQuery query) {
        User user = userService.getCurrentUser();
        query.setCompanyId(user.getCompanyId());
        Pagination<ProjectVo> pagination = projectService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("detail")
    @NeedLogin
    public ResponseObject detail(@RequestParam Long id) {
        Project project = projectService.findById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(project);
        return responseObject;
    }

    @RequestMapping("save")
    @NeedLogin
    public ResponseObject save(Project project) {
        User user = userService.getCurrentUser();
        project.setCompanyId(user.getCompanyId());
        int result = projectService.insert(project);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("update")
    @NeedLogin
    public ResponseObject update(Project project) {
        Assert.notNull(project.getId(),"id 不能为空");
        int result = projectService.updateById(project);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("deleteById")
    @NeedLogin
    public ResponseObject deleteById(@RequestParam Long id) {
        int result = projectService.deleteById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("projectConfigList")
    @NeedLogin
    public ResponseObject projectConfigList(@RequestParam Long projectId) {
//        User user = userService.getCurrentUser();
//        query.setCompanyId(user.getCompanyId());
        List<Config> dataList = configService.selectProjectList(projectId);
//        Pagination<ProjectVo> pagination = projectService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(dataList);
        return responseObject;
    }

    @RequestMapping("saveConfig")
    @NeedLogin
    public ResponseObject saveConfig(ProjectConfigParam param) {
//        User user = userService.getCurrentUser();
        Assert.notNull(param.getProjectId(),"项目id不能为空");
        Assert.notEmpty(param.getConfigList(),"配置项不能为空");
        int result = configService.saveProjectConfig(param.getProjectId(), param.getConfigList());
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;

    }

    @RequestMapping("build")
    @NeedLogin
    public ResponseObject build(@RequestParam Long projectId) {
        Long appId = null;
        User user = userService.getCurrentUser();
        if (user.getCompanyId().longValue() == 3) {  //DF
            appId = 21L;
        } else if (user.getCompanyId().longValue() == 4) {  //zh
            appId = 22L;
        }
//        String result = codeBuildService.buildApp(appId, ids);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
//        responseObject.setData(result);
        return responseObject;
    }

}
