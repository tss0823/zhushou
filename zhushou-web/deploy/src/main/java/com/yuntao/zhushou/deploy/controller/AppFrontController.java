package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.AppFrontQuery;
import com.yuntao.zhushou.model.vo.AppFrontVo;
import com.yuntao.zhushou.service.inter.AppFrontService;
import com.yuntao.zhushou.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("appFront")
public class AppFrontController extends BaseController {

    @Autowired
    private AppFrontService appFrontService;

    @Autowired
    private UserService userService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(AppFrontQuery query) {
        User user = userService.getCurrentUser();
        query.setCompanyId(user.getCompanyId());
//        query.setFront(YesNoIntType.yes.getCode());
        Pagination<AppFrontVo> pagination = appFrontService.selectFrontPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }


}
