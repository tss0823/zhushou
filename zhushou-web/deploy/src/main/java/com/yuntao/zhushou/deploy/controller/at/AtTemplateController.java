package com.yuntao.zhushou.deploy.controller.at;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.utils.ValidateUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.*;
import com.yuntao.zhushou.model.enums.AtVariableScope;
import com.yuntao.zhushou.model.param.at.AtActiveParam;
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


    @RequestMapping("saveActive")
    @NeedLogin
    public ResponseObject saveActive(@RequestParam Long templateId, AtActiveParam active) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<AtParameter> parameterList = active.getParameterList();
        int result = atActiveService.save(templateId,active, parameterList);
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("saveVariable")
    @NeedLogin
    public ResponseObject saveVariable(@RequestParam(required = false) Long templateId, @RequestParam  String key,@RequestParam  String value) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.getCurrentUser();
        AtVariable atVariable = new AtVariable();
        atVariable.setCompanyId(user.getCompanyId());
        atVariable.setKey(key);
        atVariable.setValue(value);
        atVariable.setUserId(user.getId());
        if(templateId != null){
           atVariable.setTemplateId(templateId);
           atVariable.setScope(AtVariableScope.global.getCode());
        }else{
            atVariable.setScope(AtVariableScope.pri.getCode());
        }
        int result = atVariableService.insert(atVariable);
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("collect")
    @NeedLogin
    public ResponseObject collect(@RequestParam  Long id,@RequestParam String appName,
                                  @RequestParam String mobile, @RequestParam String startTime,@RequestParam String endTime) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.getCurrentUser();
        Company company = companyService.findById(user.getCompanyId());
        AtTemplate atTemplate = atTemplateService.findById(id);
        atTemplateService.collect(id,company.getKey(),atTemplate.getModel(),appName,mobile,startTime,endTime);
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
}
