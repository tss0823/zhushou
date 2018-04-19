package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.Template;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.TemplateQuery;
import com.yuntao.zhushou.model.vo.TemplateVo;
import com.yuntao.zhushou.service.inter.AttachmentService;
import com.yuntao.zhushou.service.inter.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("template")
public class TemplateController extends BaseController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private AttachmentService attachmentService;


    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(TemplateQuery query) {
        User user = userService.getCurrentUser();
        query.setCompanyId(user.getCompanyId());
        Pagination<TemplateVo> pagination = templateService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("detail")
    @NeedLogin
    public ResponseObject detail(@RequestParam Long id) {
        Template template = templateService.findById(id);
//        Attachment attachment = attachmentService.findById(template.getAttachmentId());
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(template);
        return responseObject;
    }

    @RequestMapping("save")
    @NeedLogin
    public ResponseObject save(Template template) {
        Assert.notNull(template.getAttachmentId(),"模版不能为空");
        User user = userService.getCurrentUser();
        template.setCompanyId(user.getCompanyId());
        int result = templateService.insert(template);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("update")
    @NeedLogin
    public ResponseObject update(Template template) {
        Assert.notNull(template.getId(), "id 不能为空");
        Assert.notNull(template.getAttachmentId(),"模版不能为空");
        int result = templateService.updateById(template);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("deleteById")
    @NeedLogin
    public ResponseObject deleteById(@RequestParam Long id) {
        int result = templateService.deleteById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

}
