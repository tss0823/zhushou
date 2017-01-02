package com.yuntao.zhushou.client.controller;

import com.yuntao.zhushou.common.utils.HttpUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shengshan.tang on 2015/12/16 at 23:45
 */
@Controller
public class CommonController extends BaseController {

    int cacheSize = 800;

    //cache 使用LRU 策略
    Map<String,Object> cacheMap = new LinkedHashMap(cacheSize){
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return this.size() >= cacheSize;
        }
    };

    {
        cacheMap = Collections.synchronizedMap(cacheMap);
    }

//    @Autowired
//    private UserService userService;


    @RequestMapping("/")
    public String index() {
        return "redirect:/index.html";
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


}
