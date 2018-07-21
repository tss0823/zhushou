package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.*;
import com.yuntao.zhushou.model.domain.codeBuild.DbConfigure;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import com.yuntao.zhushou.model.query.EntityQuery;
import com.yuntao.zhushou.model.query.ProjectQuery;
import com.yuntao.zhushou.model.query.PropertyQuery;
import com.yuntao.zhushou.model.vo.EntityVo;
import com.yuntao.zhushou.service.inter.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("codeBuild")
public class CodeBuildController extends BaseController {


    @Autowired
    private AppService appService;


    @Autowired
    private CodeBuildService codeBuildService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private CodeBuildSqlService codeBuildSqlService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private BuildRecordService buildRecordService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping("getProjectByEnName")
    @NeedLogin
    public ResponseObject getProjectByEnName(@RequestParam String enName) {
//        User user = userService.getCurrentUser();
        ProjectQuery query = new ProjectQuery();
        query.setEnName(enName);
        Project project = projectService.selectOne(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(project);
        return responseObject;
    }

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(EntityQuery query) {
        User user = userService.getCurrentUser();
        query.setCompanyId(user.getCompanyId());
//        Project project = projectService.getFirst(user.getCompanyId());
//        if(project != null){
//            query.setProjectId(project.getId());
//        }
        Pagination<EntityVo> pagination = entityService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("entitySave")
    @NeedLogin
    public ResponseObject entitySave(Entity entity) {
        Assert.state(StringUtils.isNotBlank(entity.getEnName()),"英文名称不能为空");
        Assert.state(StringUtils.isNoneBlank(entity.getCnName()),"中文名称不能为空");
//        User user = userService.getCurrentUser();
        int result = entityService.insert(entity);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("entityUpdate")
    @NeedLogin
    public ResponseObject entityUpdate(Entity entity) {
        Assert.notNull(entity.getId(),"id不能为空");
        Assert.state(StringUtils.isNotBlank(entity.getEnName()),"英文名称不能为空");
        Assert.state(StringUtils.isNoneBlank(entity.getCnName()),"中文名称不能为空");
        int result = entityService.updateById(entity);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("entityDetail")
    @NeedLogin
    public ResponseObject entityDetail(@RequestParam Long id) {
        Entity entity = entityService.findById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(entity);
        return responseObject;
    }

    @RequestMapping("getEntityByEnName")
    @NeedLogin
    public ResponseObject getEntityByEnName(@RequestParam Long projectId,@RequestParam String enName) {
//        User user = userService.getCurrentUser();
        EntityQuery query = new EntityQuery();
        query.setProjectId(projectId);
        query.setEnName(enName);
        Entity entity = entityService.selectOne(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(entity);
        return responseObject;
    }

    @RequestMapping("entityDelete")
    @NeedLogin
    public ResponseObject entityDelete(@RequestParam Long id) {
        int result = entityService.deleteById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("entityCopy")
    @NeedLogin
    public ResponseObject entityCopy(@RequestParam Long id) {
        int result = codeBuildService.entityCopy(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("buildSql")
    @NeedLogin
    public ResponseObject buildSql(@RequestParam Long projectId, @RequestParam List<Long> entityIds) {
        User user = userService.getCurrentUser();
        Entity entity = entityService.findById(entityIds.get(0));
        String result = codeBuildService.buildSql(user, entity.getProjectId(), entityIds);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("buildApp")
    @NeedLogin
    public ResponseObject buildApp(@RequestParam Long projectId, @RequestParam List<Long> entityIds) {
        User user = userService.getCurrentUser();
        Entity entity = entityService.findById(entityIds.get(0));
        Long attachmentId = codeBuildService.buildApp(true, user, entity.getProjectId(), entityIds);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(attachmentId);
        return responseObject;
    }

    @RequestMapping("buildSqlSave")
    @NeedLogin
    public ResponseObject buildSqlSave(@RequestParam Long projectId,@RequestParam  String sql) {
        User user = userService.getCurrentUser();
        CodeBuildSql codeBuildSql = new CodeBuildSql();
        codeBuildSql.setProjectId(projectId);
        codeBuildSql.setCompanyId(user.getCompanyId());
        codeBuildSql.setUserId(user.getId());
        codeBuildSql.setContent(sql);
        int result = codeBuildSqlService.insert(codeBuildSql);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("propertyList")
    @NeedLogin
    public ResponseObject propertyList(PropertyQuery query) {
        List<Property> propertyList = propertyService.selectList(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(propertyList);
        return responseObject;
    }

    @RequestMapping("propertySave")
    @NeedLogin
    public ResponseObject propertySave(EntityParam entityParam) {
        Assert.notNull(entityParam.getId(),"实体id不能为空");
        Assert.notEmpty(entityParam.getPropertyList(),"属性项不能为空");
        int result = propertyService.insertBatch(entityParam.getId(),entityParam.getPropertyList());
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("getDbConfigure")
    @NeedLogin
    public ResponseObject getDbConfigure(@RequestParam Long projectId) {
        DbConfigure dbConfigure = codeBuildService.getDbConfigure(projectId);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(dbConfigure);
        return responseObject;
    }


}
