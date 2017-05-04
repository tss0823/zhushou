package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.cache.JedisService;
import com.yuntao.zhushou.common.cache.QueueService;
import com.yuntao.zhushou.common.constant.CacheConstant;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.HttpParam;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.model.domain.Host;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.vo.AutoDeployVo;
import com.yuntao.zhushou.service.inter.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by shan on 2017/5/2.
 */
@Service
public class DeployServiceImpl extends AbstService implements DeployService {


    @Autowired
    private JedisService jedisService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppService appService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private HostService hostService;

    @Autowired
    private ConfigService configService;

    private AtomicBoolean deployState = new AtomicBoolean(false);  //false 未开始，true 已开始

    @Override
    public void autoDeploy(String json) {
        //analyse json
        try{
            JSONObject jsonObject = new JSONObject(json);
            String ref = jsonObject.getString("ref");
            int index = ref.lastIndexOf("/");
            String branch = ref.substring(index + 1);
//            System.out.println("branch="+branch);
//            System.out.println("ref="+jsonObject.getString("ref"));
            String name= jsonObject.getJSONObject("project").getString("name");  //git 名称预留
            String url= jsonObject.getJSONObject("project").getString("url");
            JSONArray commits = jsonObject.getJSONArray("commits");
            JSONObject commitsJSONObject = commits.getJSONObject(0);
            JSONObject authorJsonObject = commitsJSONObject.getJSONObject("author");
            String userName = authorJsonObject.getString("name");
            String email = authorJsonObject.getString("email");
            String appName = "member";

            //自动发布人员
            User user = userService.getByEmail(email);
            //call remote method
            Long companyId = user.getCompanyId();

            String cacheValue = companyId +"_"+ appName +"_"+branch;

            AutoDeployVo autoDeployVo = new AutoDeployVo();
            autoDeployVo.setUserId(user.getId());
            autoDeployVo.setNickname(user.getNickName());
            autoDeployVo.setCompanyId(companyId);

            autoDeployVo.setUrl(url);
            autoDeployVo.setUserName(userName);
            autoDeployVo.setEmail(email);
            autoDeployVo.setAppName(appName);  //写死member
            autoDeployVo.setModel("test");  //写死test
            autoDeployVo.setBranch(branch);
            autoDeployVo.setCommits(commitsJSONObject.toString());

           boolean sismember = jedisService.getShardedJedis().sismember(CacheConstant.Deploy.autoDeplyList, cacheValue);
            if(!sismember){
                //add to set
                jedisService.getShardedJedis().sadd(CacheConstant.Deploy.autoDeplyList,cacheValue);

                //add to queue
                String jsonValue = JsonUtils.object2Json(autoDeployVo);
                queueService.add(CacheConstant.Deploy.autoDeplyList,jsonValue);
            }


        }catch (Exception e){
            throw new BizException("error!",e);
        }
    }

    @Override
    public void autoDeployTask() {
        //清空所有
        String cacheKeyList = CacheConstant.Deploy.autoDeplyList;
        Long count = jedisService.getShardedJedis().scard(cacheKeyList);
        if(count > 0){
           jedisService.getShardedJedis().spop(cacheKeyList,count);
//           count = jedisService.getShardedJedis().scard(cacheKeyList);
        }
        while (StringUtils.isNotEmpty(queueService.pop(cacheKeyList))){
        }

        //take task from queue
        while(true){
            String value = queueService.peek(cacheKeyList);
            //value为空或者正在执行
            if(StringUtils.isEmpty(value)){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                continue;
            }
            if (deployState.compareAndSet(false, true)) {  //未结束
                continue;
            }
            //
            String cacheValue = null;
            try{
                AutoDeployVo autoDeployVo = JsonUtils.json2Object(value, AutoDeployVo.class);
                cacheValue = autoDeployVo.getCompanyId() +"_"+ autoDeployVo.getAppName()+"_"+autoDeployVo.getBranch();

                // 移除set key
                jedisService.getShardedJedis().srem(cacheKeyList,cacheValue);

                // 移除list key
                queueService.pop(cacheKeyList);

                //get app
                App app = appService.findByName(autoDeployVo.getCompanyId(),autoDeployVo.getAppName());

                //get compnay
                Company company = companyService.findById(autoDeployVo.getCompanyId());

                RequestRes requestRes = new RequestRes();
                requestRes.setUrl("http://"+company.getIp()+":"+company.getPort()+"/deploy/autoCompile");
                Map<String,String> params = new HashMap<>();
                List<HttpParam> paramList = new ArrayList<>();
                params.put("userId",autoDeployVo.getUserId().toString());
                params.put("nickname",autoDeployVo.getNickname());
                params.put("codeName",app.getCodeName());
                params.put("branch",autoDeployVo.getBranch());
                params.put("model",autoDeployVo.getModel());
                String autoDeployAppName = configService.getValueByName(autoDeployVo.getCompanyId(), "autoDeployAppName");
                String[] autoDeployAppNameStrs = autoDeployAppName.split(",");
                for (String autoDeployAppNameStr : autoDeployAppNameStrs) {
                    HttpParam httpParam = new HttpParam("appNames[]", autoDeployAppNameStr);
                    paramList.add(httpParam);
                    App thisApp = appService.findByName(autoDeployVo.getCompanyId(), autoDeployAppNameStr);
                    //get ipList
                    List<Host> hostList = hostService.selectListByAppAndModel(thisApp.getId(), autoDeployVo.getModel());
                    List<String> ipList = new ArrayList<>();
                    for (Host host : hostList) {
                        ipList.add(host.getEth0());
                    }
                    httpParam = new HttpParam("ipList[]", StringUtils.join(ipList, ","));
                    paramList.add(httpParam);
                }
                //end
                String compilePropertyJson = app.getCompileProperty();
                try{
                    JSONObject jsonObject = new JSONObject(compilePropertyJson);
                    Object compileProp = jsonObject.get(autoDeployVo.getModel());
                    if(compileProp != null){
                        params.put("compileProperty",compileProp.toString());
                    }
                }catch (Exception e){
                    bisLog.error("get compile property json error",e);
                }
                requestRes.setParams(params);
                requestRes.setParamList(paramList);
                ResponseRes execute = HttpNewUtils.execute(requestRes);
                byte[] result = execute.getResult();
                bisLog.info(new String(result));

            }catch (Exception e){
                throw new BizException("error!",e);
            }finally {
                //
            }

        }



    }

    @Override
    public void changeDeployState(boolean state) {
        deployState.set(state);
    }
}
