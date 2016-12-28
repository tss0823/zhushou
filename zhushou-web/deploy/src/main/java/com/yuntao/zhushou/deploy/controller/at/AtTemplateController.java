package com.yuntao.zhushou.deploy.controller.at;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.AtActive;
import com.yuntao.zhushou.model.domain.AtParameter;
import com.yuntao.zhushou.model.domain.AtTemplate;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.AtTemplateQuery;
import com.yuntao.zhushou.model.vo.AtTemplateVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AtTemplateService;
import com.yuntao.zhushou.service.inter.LogService;
import com.yuntao.zhushou.service.inter.UserService;
import org.omg.PortableInterceptor.ACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("atTemplate")
public class AtTemplateController extends BaseController {

    @Autowired
    private AtTemplateService atTemplateService;

    @Autowired
    private UserService userService;


    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(AtTemplateQuery query) {
        Pagination<AtTemplateVo> pagination = atTemplateService.selectPage(query);
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }


    @RequestMapping("save")
    @NeedLogin
    public ResponseObject save(AtTemplateVo template,@RequestParam HashMap<String,List<AtParameter>> paramMap) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.getCurrentUser();
        template.setUserId(user.getId());
        template.setUserName(user.getNickName());
//        atTemplateService.save(template,logIds);
        return responseObject;
    }


}
