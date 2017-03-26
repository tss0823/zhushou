package com.yuntao.zhushou.common.utils;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by shengshan.tang on 11/12/2015 at 5:45 PM
 */
public class HttpUtil {

    public static boolean request(String url,Map<String,String> headers,Map<String,String> params){
//        url = "http://localhost:1024/session/book";
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(url);
            httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
            //headers
            Set<Map.Entry<String,String>> headerSet = headers.entrySet();
            String contentType = "application/x-www-form-urlencoded; charset=UTF-8";
            for(Map.Entry<String,String> entry : headerSet){
                String value = entry.getValue();
                if(StringUtils.equalsIgnoreCase(entry.getKey(),"Content-Length")){
                    continue;
                }
                if(StringUtils.equalsIgnoreCase(entry.getKey(),"content-type")){
                    if(entry.getValue().startsWith("multipart/form-data")){
                        value = contentType;
                    }
                }
                post.setHeader(entry.getKey(),value);
            }

            //params
            if(MapUtils.isNotEmpty(params)){
                List<NameValuePair> paramList = new ArrayList<>();
                Set<Map.Entry<String,String>> paramSet = params.entrySet();
                for(Map.Entry<String,String> entry : paramSet){
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                AbstractHttpEntity formEntity = new UrlEncodedFormEntity(paramList,"utf-8");
                formEntity.setContentType(contentType);
                post.setEntity(formEntity);
            }
            HttpResponse response = httpclient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return true;
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            httpclient.getConnectionManager().shutdown();
        }
        return false;

    }
}
