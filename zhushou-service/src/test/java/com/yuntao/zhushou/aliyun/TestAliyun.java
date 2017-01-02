package com.yuntao.zhushou.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.slb.model.v20140515.AddBackendServersRequest;
import com.aliyuncs.slb.model.v20140515.RemoveBackendServersRequest;
import com.yuntao.zhushou.common.constant.AppConstant;

/**
 * Created by shengshan.tang on 2016/9/9.
 */
public class TestAliyun {

    public static void removeServer() {
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", AppConstant.Aliyun.KEY, AppConstant.Aliyun.SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        try {
//            DescribeInstancesResponse response = client.getAcsResponse(describe);
            RemoveBackendServersRequest removeBackendServersRequest = new RemoveBackendServersRequest();
            removeBackendServersRequest.setLoadBalancerId("14e7190cefc-cn-hangzhou-dg-a01");
            removeBackendServersRequest.setBackendServers("[\"i-238dxw913\"]");
            HttpResponse httpResponse = client.doAction(removeBackendServersRequest);
//            System.out.println(response);
            System.out.println(httpResponse);
        }catch (ServerException e) {
            e.printStackTrace();
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static void addServer() {
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", AppConstant.Aliyun.KEY, AppConstant.Aliyun.SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        try {
//            DescribeInstancesResponse response = client.getAcsResponse(describe);
            AddBackendServersRequest addBackendServersRequest = new AddBackendServersRequest();
            addBackendServersRequest.setLoadBalancerId("14e7190cefc-cn-hangzhou-dg-a01");
            addBackendServersRequest.setBackendServers("[{\"ServerId\":\"i-238dxw913\",\"Weight\":\"100\"}]");
//            addBackendServersRequest.setBackendServers("[\"i-238dxw913\"]");
            HttpResponse httpResponse = client.doAction(addBackendServersRequest);
//            System.out.println(response);
            System.out.println(httpResponse);
        }catch (ServerException e) {
            e.printStackTrace();
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        removeServer();
        addServer();

    }
}
