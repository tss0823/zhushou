package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.HttpUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.service.inter.UserService;
import com.yuntao.zhushou.service.support.bis.HttpProxyServerSupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shengshan.tang on 2015/12/16 at 23:45
 */
@Controller
public class CommonController extends BaseController {

    @Autowired
    private HttpProxyServerSupport httpProxyServerSupport;

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

    @Autowired
    private UserService userService;


    @PostConstruct
    private void init() {
        httpProxyServerSupport.init();
    }


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

    @RequestMapping("getCityById")
    @ResponseBody
    public ResponseObject getCityByIp(@RequestParam String ip) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        Object value = cacheMap.get(ip);
        if(value != null){
            responseObject.setData(value);
            return responseObject;
        }

        List<String> lines = HttpUtils.reqGet("http://ip.taobao.com/service/getIpInfo.php?ip="+ip);
        String result = StringUtils.join(lines,"");
        responseObject.setMessage(result);
        Map map = JsonUtils.json2Object(result, HashMap.class);
        Object code = map.get("code");
        if(code == null || code.toString().equals("1")){
            responseObject.setData("无");
            cacheMap.put(ip,"无");
            return responseObject;
        }
        Map childMap = (Map) map.get("data");
        Object province = childMap.get("region");
        Object city = childMap.get("city");
        if (province == null || province.toString().equals("")) {
            Object country = childMap.get("country");
            responseObject.setData(country);
            cacheMap.put(ip,country);
            return responseObject;
        }
        String address = province + "" +city;
        cacheMap.put(ip,address);
        responseObject.setData(address);
        return responseObject;

    }

    @RequestMapping("favicon.ico")
    String favicon() {
        return "forward:/_resources/images/favicon.ico";
    }

}
