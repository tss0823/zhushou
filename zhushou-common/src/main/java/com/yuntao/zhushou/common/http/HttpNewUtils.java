package com.yuntao.zhushou.common.http;

import com.yuntao.zhushou.common.constant.AppConstant;
import com.yuntao.zhushou.common.exception.BizException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Created by shan on 2016/7/22.
 */
public class HttpNewUtils {


    private static final Logger log = LoggerFactory.getLogger(HttpNewUtils.class);

    private final static Logger stackLog = LoggerFactory.getLogger("stackLog");

    static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

    private static String userAgent = null;


    static {
        cm.setMaxTotal(20);//连接池最大并发连接数
        cm.setDefaultMaxPerRoute(5);//单路由最大并发数
        cm.setValidateAfterInactivity(100);

    }

    final static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();

    static CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieSpecRegistry(null).setConnectionManager(cm).build();

    static CookieStore cookieStore = new BasicCookieStore();

    public static ResponseRes  get(String url){
        HttpContext httpContext = new HttpClientContext();
        cookieStore.clear();
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        ResponseRes responseRes = new ResponseRes();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try{
            if(StringUtils.isNotEmpty(userAgent) && httpGet.getHeaders("user-agent") != null){
                httpGet.setHeader("User-Agent", userAgent);
            }
            response = httpclient.execute(httpGet,httpContext);
            int status = response.getStatusLine().getStatusCode();
            responseRes.setStatus(status);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            byte[] buffer = new byte[1024 * 1024];
            ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(0);
            int n = 0;
            while ((n = is.read(buffer)) > 0) {
                byteArrayBuffer.append(buffer, 0, n);
            }
            responseRes.setResult(byteArrayBuffer.toByteArray());
            responseRes.setBodyText(new String(responseRes.getResult(),"utf-8"));

            //get res header
            Header[] resHeaders = response.getAllHeaders();
            Map<String, String> headerMap = new HashMap<>();
            for (Header resHeader : resHeaders) {
                headerMap.put(resHeader.getName(), resHeader.getValue());
            }
            responseRes.setHeaders(headerMap);

        } catch (Exception e) {
            BizException bizException = new BizException("http execute failed!", e);
            if(e instanceof ConnectTimeoutException || e instanceof SocketTimeoutException){
                bizException.setCode(AppConstant.ExceptionCode.REMOTE_TIME_OUT);
            }
            throw bizException;
        }finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return responseRes;
    }


    public static ResponseRes execute(RequestRes requestRes) {
        HttpContext httpContext = new HttpClientContext();
        cookieStore.clear();
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        ResponseRes responseRes = new ResponseRes();
        String url = requestRes.getUrl();
        HttpPost httpPost = new HttpPost(url);
        HttpHost proxy = null;
        if(requestRes.isProxy()){
            proxy = new HttpHost(requestRes.getProxyHost(), requestRes.getProxyPort(), "http");
        }
        httpPost.setConfig(requestConfig);
        //处理headers
        Map<String, String> headers = requestRes.getHeaders();
        if(MapUtils.isNotEmpty(headers)){
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                if(entry.getKey().equalsIgnoreCase("content-length")){
                    continue;
                }
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        if(StringUtils.isNotEmpty(userAgent) && httpPost.getHeaders("user-agent") == null){
            httpPost.setHeader("User-Agent", userAgent);
        }
        Map<String, String> params = (Map<String, String>) requestRes.getParams();
        List<HttpParam> paramList = requestRes.getParamList();
        CloseableHttpResponse response = null;
        try{

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (MapUtils.isNotEmpty(params)) {
                Set<Map.Entry<String, String>> paramEntrySet = params.entrySet();
                for (Map.Entry<String, String> paramEntry : paramEntrySet) {
                    Object value = paramEntry.getValue();
                    nvps.add(new BasicNameValuePair(paramEntry.getKey(), value != null ? value.toString() : null));
                }
            }
            //针对 多个一样的name 比如 name[] name[]
            if(CollectionUtils.isNotEmpty(paramList)){
                for (HttpParam httpParam : paramList) {
                    nvps.add(new BasicNameValuePair(httpParam.getKey(), httpParam.getValue()));
                }
            }

            if(CollectionUtils.isNotEmpty(nvps)){
                httpPost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            }

//            post text param
            String paramText = requestRes.getParamText();
            if (StringUtils.isNotEmpty(paramText)) {
                StringEntity entity = new StringEntity(paramText,"utf-8");
                httpPost.setEntity(entity);
            }
            //post byte data
            byte[] paramByte = requestRes.getParamByte();
            if(paramByte != null ){
                ByteArrayEntity byteArrayEntity = new ByteArrayEntity(paramByte);
                httpPost.setEntity(byteArrayEntity);
            }

            response = httpclient.execute(httpPost,httpContext);
            int status = response.getStatusLine().getStatusCode();
            responseRes.setStatus(status);
//            if (status != HttpStatus.SC_OK) {
//                String errMsg = "http status="+status+",url="+url;
//                log.error(errMsg);
//                responseRes.setResult(errMsg.getBytes());
//                responseRes.setBodyText(errMsg);
//                return responseRes;
//            }
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            byte[] buffer = new byte[1024 * 1024];
            ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(0);
            int n = 0;
            while ((n = is.read(buffer)) > 0) {
                byteArrayBuffer.append(buffer, 0, n);
            }
            responseRes.setResult(byteArrayBuffer.buffer());
            responseRes.setBodyText(new String(responseRes.getResult(),"utf-8"));

            //get res header
            Header[] resHeaders = response.getAllHeaders();
            Map<String, String> headerMap = new HashMap<>();
            for (Header resHeader : resHeaders) {
                headerMap.put(resHeader.getName(), resHeader.getValue());
            }
            responseRes.setHeaders(headerMap);

        } catch (Exception e) {
            BizException bizException = new BizException("http execute failed!", e);
            if(e instanceof ConnectTimeoutException || e instanceof SocketTimeoutException){
                bizException.setCode(AppConstant.ExceptionCode.REMOTE_TIME_OUT);
            }
            throw bizException;
        }finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return responseRes;
    }

    public static void setUserAgent(String ua){
        userAgent = ua;
    }

    public static void main(String[] args) {
//        RequestRes requestRes = new RequestRes();
//        requestRes.setUrl("http://user.api.mynixihongbao.com/m/login");
//        Map<String, String> headerMap = new HashMap<>();
//        requestRes.setHeaders(headerMap);
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("mobile", "15267164682");
//        paramMap.put("password", "E10ADC3949BA59ABBE56E057F20F883E");
//        requestRes.setParams(paramMap);
//
//        ResponseRes responseRes = execute(requestRes);
//        log.info(responseRes.getStatus().toString());


        RequestRes requestRes = new RequestRes();
        requestRes.setUrl("http://test.doublefit.cn:8083/genFile/download.htm?id=208");
        ResponseRes responseRes = execute(requestRes);
        log.info(responseRes.getStatus().toString());
        String tempPath = System.getProperty("java.io.tmpdir");
        log.info("tempPath="+tempPath);
        String filePath = tempPath + ""+System.currentTimeMillis()+".zip";
        File file = new File(filePath);
        try {
            FileUtils.writeByteArrayToFile(file,responseRes.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
