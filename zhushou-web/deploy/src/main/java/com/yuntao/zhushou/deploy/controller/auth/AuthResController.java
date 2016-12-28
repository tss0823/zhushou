package com.yuntao.zhushou.deploy.controller.auth;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.AuthRes;
import com.yuntao.zhushou.model.query.AuthResQuery;
import com.yuntao.zhushou.model.vo.AuthResVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AuthResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("authRes")
public class AuthResController extends BaseController {


    @Autowired
    private AuthResService authResService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(AuthResQuery query) {
        Pagination<AuthResVo> pagination = authResService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }



}
