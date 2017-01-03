package com.yuntao.zhushou.common.http;

import com.yuntao.zhushou.common.utils.ExceptionUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

/**
 * Created by shan on 2016/7/22.
 */
public class HttpNewUtils {


    private static final Logger log = LoggerFactory.getLogger(HttpNewUtils.class);

    static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    static {
        cm.setMaxTotal(20);//连接池最大并发连接数
        cm.setDefaultMaxPerRoute(5);//单路由最大并发数
    }

    final static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();

    static CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();

//    static Map<Res,byte []> contentMap = new ConcurrentHashMap();

    public static ResponseRes get(String url){
        RequestRes requestRes = new RequestRes();
        requestRes.setUrl(url);
        return execute(requestRes);
    }

    public static ResponseRes execute(RequestRes requestRes)  {
        ResponseRes responseRes = new ResponseRes();
        String url = requestRes.getUrl();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        //处理headers
        Map<String, String> headers = requestRes.getHeaders();
        if (MapUtils.isNotEmpty(headers)) {
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                if(entry.getKey().equals("content-length")){
                    continue;
                }
                httpPost.setHeader(entry.getKey(),entry.getValue());
            }
        }
        Map<String, String> params = requestRes.getParams();
        try{

            if (MapUtils.isNotEmpty(params)) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                Set<Map.Entry<String, String>> paramEntrySet = params.entrySet();
                for (Map.Entry<String, String> paramEntry : paramEntrySet) {
                    nvps.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            }
            log.info("执行 http "+requestRes.getUrl());
            CloseableHttpResponse response = httpclient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            responseRes.setStatus(status);
            if(status != HttpStatus.SC_OK){
                log.error("http status="+status+",url="+url);
                return responseRes;
            }
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            byte [] buffer = new byte[1024*1024];
            ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(0);
            int n = 0;
            while((n = is.read(buffer)) > 0){
                byteArrayBuffer.append(buffer,0,n);
            }
            responseRes.setResult(byteArrayBuffer.toByteArray());

            //get res header
            Header[] resHeaders = response.getAllHeaders();
            Map<String, String> headerMap = new HashMap<>();
            for (Header resHeader : resHeaders) {
               headerMap.put(resHeader.getName(),resHeader.getValue()) ;
            }

            responseRes.setHeaders(headerMap);

        }catch (Exception e){
            responseRes.setResult(ExceptionUtils.getPrintStackTrace(e).getBytes());
//            throw new RuntimeException("http execute failed!",e);
        }
        return responseRes;
    }

    public static void main(String[] args) {
        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://user.api.yuntaohongbao.com/m/login");
        Map<String,String> headerMap = new HashMap<>();
        requestRes.setHeaders(headerMap);
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("mobile","15267164682");
        paramMap.put("password","E10ADC3949BA59ABBE56E057F20F883E");
        requestRes.setParams(paramMap);

        ResponseRes responseRes = execute(requestRes);
        log.info(responseRes.getStatus().toString());
    }
}
