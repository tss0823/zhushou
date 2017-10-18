package com.yuntao.zhushou.deploy.controller.at;

import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.utils.ValidateUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.AtVariable;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.enums.AtVariableScope;
import com.yuntao.zhushou.model.enums.YesNoIntType;
import com.yuntao.zhushou.model.query.AtVariableQuery;
import com.yuntao.zhushou.model.vo.AtVariableVo;
import com.yuntao.zhushou.service.inter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("atVariable")
public class AtVariableController extends BaseController {

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
    public ResponseObject list(AtVariableQuery query) {
        User user = userService.getCurrentUser();
        query.setCompanyId(user.getCompanyId());
        Pagination<AtVariableVo> pagination = atVariableService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("detail")
    @NeedLogin
    public ResponseObject detail(@RequestParam Long id) {
        AtVariable atVariable = atVariableService.findById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(atVariable);
        return responseObject;
    }


    @RequestMapping("save")
    @NeedLogin
    public ResponseObject save(AtVariable variable) {
        ValidateUtils.notEmpty(variable.getKey(),"变量名不能为空");
        ValidateUtils.notEmpty(variable.getValue(),"变量值不能为空");
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.getCurrentUser();
        variable.setUserId(user.getId());
        variable.setCompanyId(user.getCompanyId());
        try{
            if(variable.getTemplateId() != null){
                variable.setScope(AtVariableScope.pri.getCode());
            }else{
                variable.setScope(AtVariableScope.global.getCode());
            }
            variable.setStatus(YesNoIntType.yes.getCode());
            atVariableService.insert(variable);
        }catch (Exception e){
            throw new BizException("已存在相同的名称");
        }
        return responseObject;
    }

    @RequestMapping("update")
    @NeedLogin
    public ResponseObject update(AtVariable variable) {
        ValidateUtils.notNull(variable.getId(),"变量ID不能为空");
        ValidateUtils.notEmpty(variable.getKey(),"变量名不能为空");
        ValidateUtils.notEmpty(variable.getValue(),"变量值不能为空");
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.getCurrentUser();
        variable.setUserId(user.getId());
        variable.setCompanyId(user.getCompanyId());
        try{
            if(variable.getTemplateId() != null){
                variable.setScope(AtVariableScope.pri.getCode());
            }else{
                variable.setScope(AtVariableScope.global.getCode());
            }
            variable.setStatus(YesNoIntType.yes.getCode());
            atVariableService.updateById(variable);
        }catch (Exception e){
            throw new BizException("已存在相同的名称");
        }
        return responseObject;
    }

    @RequestMapping("deleteById")
    @NeedLogin
    public ResponseObject deleteById(@RequestParam Long id) {
        //update
        AtVariable variable = atVariableService.findById(id);
        variable.setKey(variable.getKey()+"_"+System.currentTimeMillis());
        atVariableService.updateById(variable);
        atVariableService.deleteById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        return responseObject;
    }


}
