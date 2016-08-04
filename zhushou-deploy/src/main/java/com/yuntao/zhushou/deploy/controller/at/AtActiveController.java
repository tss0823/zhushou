package com.yuntao.zhushou.deploy.controller.at;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.AtActive;
import com.yuntao.zhushou.model.query.AtActiveQuery;
import com.yuntao.zhushou.model.vo.AtActiveVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AtActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("atActive")
public class AtActiveController extends BaseController {


    @Autowired
    private AtActiveService atActiveService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(AtActiveQuery query) {
        Pagination<AtActiveVo> pagination = atActiveService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }



}
