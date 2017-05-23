package com.yuntao.zhushou.service.support.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.rds.model.v20140815.DescribeBinlogFilesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeBinlogFilesResponse;
import com.aliyuncs.slb.model.v20140515.AddBackendServersRequest;
import com.aliyuncs.slb.model.v20140515.RemoveBackendServersRequest;
import com.yuntao.zhushou.common.constant.AppConstant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shengshan.tang on 2016/9/9.
 */
@Component
public class SlbServerReloadService {

    static IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", AppConstant.Aliyun.KEY, AppConstant.Aliyun.SECRET);
    static IAcsClient client = new DefaultAcsClient(profile);

    private Map<String,String> serverMap = new HashMap<>();
    {
        serverMap.put("10.24.36.133","i-238dxw913");
        serverMap.put("10.24.26.251","i-23hrkusp1");
        serverMap.put("10.24.27.47","i-23ymxyouu");
        serverMap.put("10.174.108.45","i-2376uvgcv");
        serverMap.put("10.174.110.159","i-23g0s62vy");
    }


    public void removeServer(String innerIp) {
        try {
            RemoveBackendServersRequest removeBackendServersRequest = new RemoveBackendServersRequest();
            removeBackendServersRequest.setLoadBalancerId("14e7190cefc-cn-hangzhou-dg-a01");
            removeBackendServersRequest.setBackendServers("[\""+ serverMap.get(innerIp)+"\"]");
            HttpResponse httpResponse = client.doAction(removeBackendServersRequest);
            System.out.println(httpResponse);
            if (httpResponse.getStatus() != 200) {
                throw new RuntimeException("移除节点失败，status="+httpResponse.getStatus()+",detailInfo="+httpResponse.getUrl());
            }
        }catch (Exception e) {
            throw new RuntimeException("移除节点失败",e);
        }
    }

    public void addServer(String innerIp) {
        try {
            AddBackendServersRequest addBackendServersRequest = new AddBackendServersRequest();
            addBackendServersRequest.setLoadBalancerId("14e7190cefc-cn-hangzhou-dg-a01");
            addBackendServersRequest.setBackendServers("[{\"ServerId\":\""+serverMap.get(innerIp)+"\",\"Weight\":\"100\"}]");
            HttpResponse httpResponse = client.doAction(addBackendServersRequest);
            if (httpResponse.getStatus() != 200) {
                throw new RuntimeException("添加节点失败，status="+httpResponse.getStatus()+",detailInfo="+httpResponse.getUrl());
            }
        }catch (Exception e) {
            throw new RuntimeException("添加节点失败",e);
        }
    }

    public static void main(String[] args) {
//        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        DescribeBinlogFilesRequest request = new DescribeBinlogFilesRequest();
        request.setDBInstanceId("rm-bp128xoehj28o314y");
        request.setStartTime("2017-05-15T08:00:00Z");
        request.setEndTime("2017-05-16T23:00:00Z");
        try {
//            DescribeInstancesResponse response = client.getAcsResponse(request);
            DescribeBinlogFilesResponse response = client.getAcsResponse(request);
            System.out.println(response);
            List<DescribeBinlogFilesResponse.BinLogFile> items = response.getItems();
            for (DescribeBinlogFilesResponse.BinLogFile item : items) {
                String downloadLink = item.getDownloadLink();
                String logBeginTime = item.getLogBeginTime();
                String logEndTime = item.getLogEndTime();
                System.out.println();
                System.out.println("startTime="+logBeginTime+",endTime="+logEndTime);
                System.out.println(downloadLink);
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }


    }
}
