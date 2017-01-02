package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.DeployLogQuery;
import com.yuntao.zhushou.model.vo.DeployLogVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.service.inter.DeployLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("deployLog")
public class DeployLogController extends BaseController {

    @Autowired
    private DeployLogService deployLogService;


    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(DeployLogQuery query) {
        User user = userService.getCurrentUser();
        query.setCompanyId(user.getCompanyId());
        Pagination<DeployLogVo> pagination = deployLogService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("getDetail")
    @NeedLogin
    public ResponseObject getHostListByAppAndModel(@RequestParam Long id) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        DeployLogVo deployLogVo = deployLogService.findDetailById(id);
        responseObject.setData(deployLogVo);
        return responseObject;
    }

}
