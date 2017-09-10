package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.domain.codeBuild.DbConfigure;
import com.yuntao.zhushou.model.domain.codeBuild.Entity;
import com.yuntao.zhushou.model.domain.codeBuild.Property;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import com.yuntao.zhushou.model.query.codeBuild.EntityQuery;
import com.yuntao.zhushou.model.query.codeBuild.PropertyQuery;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.CodeBuildService;
import com.yuntao.zhushou.service.inter.ConfigService;
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

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(EntityQuery query) {
        User user = userService.getCurrentUser();
        if (user.getCompanyId().longValue() == 3) {  //DF
            query.setAppId(21L);
        } else if (user.getCompanyId().longValue() == 4) {  //zh
            query.setAppId(22L);
        }
        Pagination<Entity> pagination = codeBuildService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("entitySave")
    @NeedLogin
    public ResponseObject entitySave(Entity entity) {
        Assert.state(StringUtils.isNotBlank(entity.getEnName()),"英文名称不能为空");
        Assert.state(StringUtils.isNoneBlank(entity.getCnName()),"中文名称不能为空");
        User user = userService.getCurrentUser();
        if (user.getCompanyId().longValue() == 3) {  //DF
            entity.setAppId(21L);
        } else if (user.getCompanyId().longValue() == 4) {  //zh
            entity.setAppId(22L);
        }
        int result = codeBuildService.entitySave(entity);
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
        int result = codeBuildService.entityUpdate(entity);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("entityDetail")
    @NeedLogin
    public ResponseObject entityDetail(@RequestParam Long id) {
        Entity entity = codeBuildService.entityDetail(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(entity);
        return responseObject;
    }

    @RequestMapping("getEntityByEnName")
    @NeedLogin
    public ResponseObject getEntityByEnName(@RequestParam String enName) {
        Entity entity = codeBuildService.getEntityByEnName(enName);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(entity);
        return responseObject;
    }

    @RequestMapping("entityDelete")
    @NeedLogin
    public ResponseObject entityDelete(@RequestParam Long id) {
        int result = codeBuildService.entityDelete(id);
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
    public ResponseObject buildSql(@RequestParam String ids) {
        Long appId = null;
        User user = userService.getCurrentUser();
        if (user.getCompanyId().longValue() == 3) {  //DF
            appId = 21L;
        } else if (user.getCompanyId().longValue() == 4) {  //zh
            appId = 22L;
        }
        String result = codeBuildService.buildSql(appId, ids);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("buildApp")
    @NeedLogin
    public ResponseObject buildApp( @RequestParam String ids) {
        Long appId = null;
        User user = userService.getCurrentUser();
        if (user.getCompanyId().longValue() == 3) {  //DF
            appId = 21L;
        } else if (user.getCompanyId().longValue() == 4) {  //zh
            appId = 22L;
        }
        String result = codeBuildService.buildApp(appId, ids);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }


    @RequestMapping("propertyList")
    @NeedLogin
    public ResponseObject propertyList(PropertyQuery query) {
        List<Property> propertyList = codeBuildService.propertyList(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(propertyList);
        return responseObject;
    }

    @RequestMapping("propertySave")
    @NeedLogin
    public ResponseObject propertySave(EntityParam entityParam) {
        Assert.notNull(entityParam.getId(),"实体id不能为空");
        Assert.notEmpty(entityParam.getPropertyList(),"属性项不能为空");
        int result = codeBuildService.propertySave(entityParam);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("getDbConfigure")
    @NeedLogin
    public ResponseObject getDbConfigure() {
        Long appId = null;
        User user = userService.getCurrentUser();
        if (user.getCompanyId().longValue() == 3) {  //DF
            appId = 21L;
        } else if (user.getCompanyId().longValue() == 4) {  //zh
            appId = 22L;
        }
        DbConfigure dbConfigure = codeBuildService.getDbConfigure(appId);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(dbConfigure);
        return responseObject;
    }

}
