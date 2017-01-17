package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.utils.SerializeNewUtil;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.service.inter.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.util.StringTokenizer;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("cache")
public class CacheController extends BaseController {

    @Autowired
    private ConfigService configService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list() {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        return responseObject;
    }

    @RequestMapping("getCache")
    @NeedLogin
    public ResponseObject getCache(@RequestParam String key, String field, @RequestParam  String type) {
        User user = userService.getCurrentUser();
        StringBuilder sb = new StringBuilder();
//        String appName = AppConfigUtils.getValue("appName");
        String newKey = key;
//        if(StringUtils.isNotEmpty(appName)){
//            newKey = appName;
//        }
//        newKey += "_"+key;
//        sb.append("query key="+newKey+"\r\n\r\n");
        String redisShardConfig = configService.getValueByName(user.getCompanyId(), "redis.info");
        StringTokenizer st = new StringTokenizer(redisShardConfig,",");
        while(st.hasMoreElements()){
            Object ele = st.nextElement();
            if(ele == null){
                continue;
            }
            String ipports = ele.toString().split(",")[0];
            String ip = ipports.split(":")[0];
            String port = ipports.split(":")[1];
            String pwd = ipports.split(":")[2];
            JedisShardInfo jedisShardInfo = new JedisShardInfo(ip, port);
            jedisShardInfo.setPassword(pwd);
//            sb.append("ip="+ip+",port="+port+",result=");
            try{
                Jedis client = new Jedis(jedisShardInfo);

                if(type.equals("str")) {
                    String value = client.get(newKey);
                    sb.append(value);
                }else if(type.equals("hash")){
                    String value = client.hget(newKey,field);
                    sb.append(value);
                }else {  //byte
                    byte [] bs = client.get(newKey.getBytes());
                    Object obj = SerializeNewUtil.unserialize(bs);
                    String str = JsonUtils.object2Json(obj);
                    sb.append(str);
                }
//                sb.append(",success");
            }catch (Exception  e){
                sb.append("search failed,message="+e.getMessage());
            }
            sb.append("\r\n");
        }
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(sb.toString());
        return responseObject;

    }

    @RequestMapping("/cache/delCache")
    @ResponseBody
    @NeedLogin
    public ResponseObject delCache(@RequestParam String key,String field,@RequestParam  String type) {
        User user = userService.getCurrentUser();
        StringBuilder sb = new StringBuilder();
        String newKey = key;
        sb.append("query key="+newKey+"\r\n\r\n");
        String redisShardConfig = configService.getValueByName(user.getCompanyId(), "redis.info");
        StringTokenizer st = new StringTokenizer(redisShardConfig,"|");
        while(st.hasMoreElements()){
            Object ele = st.nextElement();
            if(ele == null){
                continue;
            }
            String ipports = ele.toString().split(",")[0];
            String ip = ipports.split(":")[0];
            String port = ipports.split(":")[1];
            String pwd = ipports.split(":")[2];
            JedisShardInfo jedisShardInfo = new JedisShardInfo(ip, port);
            jedisShardInfo.setPassword(pwd);
//            sb.append("ip="+ip+",port="+port);
            try{
                Jedis client = new Jedis(jedisShardInfo);
                if(type.equals("str")) {
                    client.del(newKey);
                }else if(type.equals("hash")){
                    client.hdel(newKey,field);
                }else {
                    client.del(newKey);
                }
//                sb.append(",success");
            }catch (Exception  e){
                sb.append("del failed,message="+e.getMessage());
            }
            sb.append("\r\n");
        }
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(sb.toString());
        return responseObject;

    }






}
