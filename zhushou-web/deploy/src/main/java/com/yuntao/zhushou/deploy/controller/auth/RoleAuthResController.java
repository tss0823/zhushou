package com.yuntao.zhushou.deploy.controller.auth;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.query.RoleAuthResQuery;
import com.yuntao.zhushou.model.vo.RoleAuthResVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.service.inter.RoleAuthResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("roleAuthRes")
public class RoleAuthResController extends BaseController {


    @Autowired
    private RoleAuthResService roleAuthResService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(RoleAuthResQuery query) {
        Pagination<RoleAuthResVo> pagination = roleAuthResService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }



}
