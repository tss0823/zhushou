package com.yuntao.zhushou.deploy.controller.auth;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.query.RoleQuery;
import com.yuntao.zhushou.model.vo.RoleVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.service.inter.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {


    @Autowired
    private RoleService roleService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(RoleQuery query) {
        Pagination<RoleVo> pagination = roleService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }



}
