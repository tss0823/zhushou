package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.common.utils.HttpUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.model.domain.WarnEvent;
import com.yuntao.zhushou.model.domain.WarnEventResult;
import com.yuntao.zhushou.model.enums.YesNoIntType;
import com.yuntao.zhushou.model.query.WarnEventQuery;
import com.yuntao.zhushou.model.query.WarnEventResultQuery;
import com.yuntao.zhushou.service.inter.DeployService;
import com.yuntao.zhushou.service.inter.UserService;
import com.yuntao.zhushou.service.inter.WarnEventResultService;
import com.yuntao.zhushou.service.inter.WarnEventService;
import com.yuntao.zhushou.service.support.bis.HttpProxyServerSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by shengshan.tang on 2015/12/16 at 23:45
 */
@Controller
public class CommonController extends BaseController {

    @Autowired
    private HttpProxyServerSupport httpProxyServerSupport;

    @Autowired
    private DeployService deployService;

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

    @Autowired
    private WarnEventService warnEventService;

    @Autowired
    private WarnEventResultService warnEventResultService;


    @PostConstruct
    private void init() {
//        httpProxyServerSupport.init();


        //自动发布任务处理
        new Thread(new Runnable() {
            @Override
            public void run() {
                deployService.autoDeployTask();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        WarnEventQuery query = new WarnEventQuery();
                        query.setStatus(YesNoIntType.no.getCode());
                        query.setExecTimeEnd(DateUtil.getFmtYMDHMS(new Date().getTime()));
                        List<WarnEvent> warnEventList = warnEventService.selectList(query);
                        if (CollectionUtils.isEmpty(warnEventList)) {
                            Thread.sleep(5000);  //5秒钟
                        }
                        warnEventService.sendTask(warnEventList);
                    }catch (Exception e){
                        bisLog.error("send warn event msg failed!",e);
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        WarnEventResultQuery query = new WarnEventResultQuery();
                        query.setStatus(YesNoIntType.no.getCode());
                        query.setMaxTryCount(3);
                        List<WarnEventResult> warnEventResultList = warnEventResultService.selectList(query);
                        if (CollectionUtils.isEmpty(warnEventResultList)) {
                            Thread.sleep(60 * 1000);  //一分钟
                        }
                        warnEventService.tryErrorSendTask(warnEventResultList);
                    }catch (Exception e){
                        bisLog.error("send warn event msg failed!",e);
                    }
                }
            }
        }).start();

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
