package com.yuntao.zhushou.deploy.controller.at;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.AtParameter;
import com.yuntao.zhushou.model.query.AtParameterQuery;
import com.yuntao.zhushou.model.vo.AtParameterVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AtParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("atParameter")
public class AtParameterController extends BaseController {


    @Autowired
    private AtParameterService atParameterService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(AtParameterQuery query) {
        Pagination<AtParameterVo> pagination = atParameterService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }



}
