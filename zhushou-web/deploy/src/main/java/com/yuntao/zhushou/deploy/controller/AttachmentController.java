package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.HttpFileDownloadUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.Attachment;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.AttachmentQuery;
import com.yuntao.zhushou.model.vo.AttachmentVo;
import com.yuntao.zhushou.service.inter.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("attachment")
public class AttachmentController extends BaseController {

    @Autowired
    private AttachmentService attachmentService;


    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(AttachmentQuery query) {
        User user = userService.getCurrentUser();
        query.setCompanyId(user.getCompanyId());
        Pagination<AttachmentVo> pagination = attachmentService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("detail")
    @NeedLogin
    public ResponseObject detail(@RequestParam Long id) {
        Attachment attachment = attachmentService.findById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(attachment);
        return responseObject;
    }


    @RequestMapping("deleteById")
    @NeedLogin
    public ResponseObject deleteById(@RequestParam Long id) {
        int result = attachmentService.deleteById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

    @RequestMapping("save")
    @NeedLogin
    public ResponseObject save(@RequestParam MultipartFile tplFile) {
        User user = userService.getCurrentUser();
        Attachment attachment = new Attachment();
        attachment.setCompanyId(user.getCompanyId());
        attachment.setName(tplFile.getOriginalFilename());
        try {
            attachment.setContent(tplFile.getBytes());
        } catch (IOException e) {
            throw new BizException("get io bytes failed!",e);
        }
        int result = attachmentService.insert(attachment);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(attachment);
        return responseObject;
    }

    @RequestMapping("download")
    @NeedLogin
    public void download(@RequestParam Long id, HttpServletResponse response) {
//        User user = userService.getCurrentUser();
        Attachment attachment = attachmentService.findById(id);
        HttpFileDownloadUtils.download(attachment.getContent(),attachment.getName(),response);
    }
}
