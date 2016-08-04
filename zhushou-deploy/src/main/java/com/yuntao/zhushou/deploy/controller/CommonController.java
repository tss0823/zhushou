package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.HttpUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by shengshan.tang on 2015/12/16 at 23:45
 */
@Controller
public class CommonController extends BaseController {


    @Autowired
    private UserService userService;


    @RequestMapping("/")
    public String index() {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login.html";
        } else {
            return "redirect:/list.html";

        }

    }

    @RequestMapping("checkServerStatus")
    @ResponseBody
    public ResponseObject checkServerStatus(@RequestParam String ip,@RequestParam Integer port) {
        String checkUrl = "http://"+ip+":"+port+"/checkServerStatus" ;
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<String> lines = HttpUtils.reqGet(checkUrl);
        String result = StringUtils.join(lines,",");
        if(!StringUtils.equals(result,"checkServerStatusIsOK")){
            responseObject.setSuccess(false);
            responseObject.setMessage(result);
        }
        return responseObject;

    }

    @RequestMapping("favicon.ico")
    String favicon() {
        return "forward:/_resources/images/favicon.ico";
    }

}
