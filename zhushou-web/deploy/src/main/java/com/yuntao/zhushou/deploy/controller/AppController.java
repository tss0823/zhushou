package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.Host;
import com.yuntao.zhushou.model.domain.Project;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.enums.LogModel;
import com.yuntao.zhushou.model.query.AppQuery;
import com.yuntao.zhushou.model.vo.AppVo;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.HostService;
import com.yuntao.zhushou.service.inter.ProjectService;
import com.yuntao.zhushou.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("app")
public class AppController extends BaseController {

    @Autowired
    private AppService appService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private HostService hostService;

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
        AppVo appVo = BeanUtils.beanCopy(app,AppVo.class);
        List<Host> testHostList = hostService.selectListByAppAndModel(id, LogModel.TEST.getCode());
        appVo.setTestHostList(testHostList);
        List<Host> prodHostList = hostService.selectListByAppAndModel(id, LogModel.PROD.getCode());
        appVo.setProdHostList(prodHostList);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(appVo);
        return responseObject;
    }

    @RequestMapping("save")
    @NeedLogin
    public ResponseObject save(App app,@RequestParam  List<Long> testHostIds,@RequestParam List<Long> prodHostIds) {
        User user = userService.getCurrentUser();
        app.setCompanyId(user.getCompanyId());
        Assert.notNull(app.getProjectId(),"项目不能为空");
        int result = appService.saveOrUpdate(app,testHostIds,prodHostIds);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("update")
    @NeedLogin
    public ResponseObject update(App app,@RequestParam List<Long> testHostIds,@RequestParam List<Long> prodHostIds){
        Assert.notNull(app.getId(),"id 不能为空");
        int result = appService.saveOrUpdate(app,testHostIds,prodHostIds);
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
