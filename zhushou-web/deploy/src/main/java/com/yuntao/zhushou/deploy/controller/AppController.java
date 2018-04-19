package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.Project;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.AppQuery;
import com.yuntao.zhushou.model.vo.AppVo;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.ProjectService;
import com.yuntao.zhushou.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app")
public class AppController extends BaseController {

    @Autowired
    private AppService appService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(AppQuery query) {
        User user = userService.getCurrentUser();
        query.setCompanyId(user.getCompanyId());
        if(query.getProjectId() == null){
            Project project = projectService.getFirst(user.getCompanyId());
            if (project != null) {
                query.setProjectId(project.getId());
            }
        }
        Pagination<AppVo> pagination = appService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("detail")
    @NeedLogin
    public ResponseObject detail(@RequestParam Long id) {
        App app = appService.findById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(app);
        return responseObject;
    }

    @RequestMapping("save")
    @NeedLogin
    public ResponseObject save(App app) {
        User user = userService.getCurrentUser();
        app.setCompanyId(user.getCompanyId());
        Assert.notNull(app.getProjectId(),"项目不能为空");
        int result = appService.insert(app);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("update")
    @NeedLogin
    public ResponseObject update(App app) {
        Assert.notNull(app.getId(),"id 不能为空");
        int result = appService.updateById(app);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("deleteById")
    @NeedLogin
    public ResponseObject deleteById(@RequestParam Long id) {
        int result = appService.deleteById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }


}
