package com.yuntao.zhushou.deploy.controller.at;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.utils.ValidateUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.*;
import com.yuntao.zhushou.model.param.at.AtActiveParam;
import com.yuntao.zhushou.model.query.AtActiveQuery;
import com.yuntao.zhushou.model.query.AtTemplateQuery;
import com.yuntao.zhushou.model.vo.AtTemplateVo;
import com.yuntao.zhushou.service.inter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("atTemplate")
public class AtTemplateController extends BaseController {

    @Autowired
    private AtTemplateService atTemplateService;

    @Autowired
    private AtActiveService atActiveService;

    @Autowired
    private AtParameterService atParameterService;

    @Autowired
    private AtVariableService atVariableService;

    @Autowired
    private CompanyService companyService;




    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(AtTemplateQuery query) {
        User user = userService.getCurrentUser();
        query.setCompanyId(user.getCompanyId());
        Pagination<AtTemplateVo> pagination = atTemplateService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("detail")
    @NeedLogin
    public ResponseObject detail(@RequestParam Long id) {
        AtTemplateVo templateVo = atTemplateService.getTemplateVo(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(templateVo);
        return responseObject;
    }

    @RequestMapping("findById")
    @NeedLogin
    public ResponseObject findById(@RequestParam Long id) {
        AtTemplate template = atTemplateService.findById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(template);
        return responseObject;
    }


    @RequestMapping("saveTemplate")
    @NeedLogin
    public ResponseObject saveTemplate(AtTemplateVo template) {
        ValidateUtils.notEmpty(template.getName(),"模版名称不能为空");
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.getCurrentUser();
        template.setUserId(user.getId());
        template.setUserName(user.getNickName());
        template.setCompanyId(user.getCompanyId());
        atTemplateService.insert(template);
        return responseObject;
    }

    @RequestMapping("updateTemplate")
    @NeedLogin
    public ResponseObject updatTemplate(AtTemplateVo template) {
        ValidateUtils.notNull(template.getId(),"模版ID不能为空");
        ValidateUtils.notEmpty(template.getName(),"模版名称不能为空");
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.getCurrentUser();
        template.setUserId(user.getId());
        template.setUserName(user.getNickName());
        atTemplateService.updateById(template);
        return responseObject;
    }

    @RequestMapping("deleteTemplate")
    @NeedLogin
    public ResponseObject deleteTemplate(@RequestParam Long id) {
        atTemplateService.deleteById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        return responseObject;
    }


    @RequestMapping("saveActive")
    @NeedLogin
    public ResponseObject saveActive(@RequestParam Long templateId, AtActiveParam active) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<AtParameter> parameterList = active.getParameterList();
        int result = atActiveService.save(templateId,active, parameterList);
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("updateActive")
    @NeedLogin
    public ResponseObject updateActive(@RequestParam Long templateId, AtActiveParam active) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<AtParameter> parameterList = active.getParameterList();
        int result = atActiveService.update(templateId,active);
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("updateActiveParamList")
    @NeedLogin
    public ResponseObject updateActiveParamList(@RequestParam Long activeId,AtActiveParam active) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<AtParameter> parameterList = active.getParameterList();
        int result = atParameterService.updateParamList(activeId,parameterList);
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("updateSingleActive")
    @NeedLogin
    public ResponseObject updateSingleActive(@RequestParam Long activeId, @RequestParam  String url) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        AtActive active = atActiveService.findById(activeId);
        active.setUrl(url);
        int result = atActiveService.updateById(active);
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("deleteActive")
    @NeedLogin
    public ResponseObject deleteActive(@RequestParam Long activeId) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        int result = atActiveService.deleteById(activeId);
        responseObject.setData(result);
        return responseObject;
    }


    @RequestMapping("collect")
    @NeedLogin
    public ResponseObject collect(@RequestParam  Long id,@RequestParam Integer type,@RequestParam(required = false) String appName,
                                  @RequestParam String mobile, @RequestParam String startTime,@RequestParam String endTime,
                                  @RequestParam List<String> logStackIds, @RequestParam(required = false) Integer orderIndex) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.getCurrentUser();
        Company company = companyService.findById(user.getCompanyId());
        AtTemplate atTemplate = atTemplateService.findById(id);
        atTemplateService.collect(id,company.getKey(),atTemplate.getModel(),type,appName,mobile,startTime,endTime,logStackIds,orderIndex);
        return responseObject;
    }

    @RequestMapping("saveActiveSort")
    @NeedLogin
    public ResponseObject saveActiveSort(@RequestParam Long templateId,@RequestParam List<Long> activeIds) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        atTemplateService.saveActiveSort(templateId,activeIds);
        return responseObject;
    }

    @RequestMapping("start")
    @NeedLogin
    public ResponseObject start(final @RequestParam Long id) {
        final User user = userService.getCurrentUser();
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    atTemplateService.start(id, user);

                }catch (Exception e){
                    throw e;
                }
            }
        }).start();
        return responseObject;
    }

    @RequestMapping("activeList")
    @NeedLogin
    public ResponseObject activeList() {
//        AtActiveQuery atActiveQuery = new AtActiveQuery();
        List<AtActive> activeList = atActiveService.selectList(new AtActiveQuery());
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(activeList);
        return responseObject;
    }
}
