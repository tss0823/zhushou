package com.yuntao.zhushou.web.task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by shengshan.tang on 2015/12/16 at 23:45
 */
@Controller
public class CommonController {

    @RequestMapping("checkServerStatus")
    @ResponseBody
    public String checkServerStatus() {
        return "checkServerStatusIsOK";
    }


}
