package com.yuntao.zhushou.deploy.controller.at;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.query.AtActiveInstQuery;
import com.yuntao.zhushou.model.vo.AtActiveInstVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AtActiveInstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("atActiveInst")
public class AtActiveInstController extends BaseController {


    @Autowired
    private AtActiveInstService atActiveInstService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(AtActiveInstQuery query) {
        Pagination<AtActiveInstVo> pagination = atActiveInstService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }




}
