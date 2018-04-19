package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.BuildRecord;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.BuildRecordQuery;
import com.yuntao.zhushou.model.vo.BuildRecordVo;
import com.yuntao.zhushou.service.inter.BuildRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("buildRecord")
public class BuildRecordController extends BaseController {

    @Autowired
    private BuildRecordService buildRecordService;


    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(BuildRecordQuery query) {
        User user = userService.getCurrentUser();
        query.setCompanyId(user.getCompanyId());
        Pagination<BuildRecordVo> pagination = buildRecordService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("detail")
    @NeedLogin
    public ResponseObject detail(@RequestParam Long id) {
        BuildRecord buildRecord = buildRecordService.findById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(buildRecord);
        return responseObject;
    }


    @RequestMapping("deleteById")
    @NeedLogin
    public ResponseObject deleteById(@RequestParam Long id) {
        int result = buildRecordService.deleteById(id);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(result);
        return responseObject;
    }

}
