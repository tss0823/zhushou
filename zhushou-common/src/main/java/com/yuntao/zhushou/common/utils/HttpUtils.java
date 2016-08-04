package com.yuntao.zhushou.common.utils;

import com.yuntao.zhushou.common.exception.BizException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by shengshan.tang on 2015/12/14 at 20:53
 */
public class HttpUtils {

    public static String getRpcJson(String reqUrl) {
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setConnectTimeout(60000);
            is = conn.getInputStream();
            List<String> strList = IOUtils.readLines(is, "utf-8");
            return StringUtils.join(strList, "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
            conn.disconnect();
        }
    }

    public static String getRpcJson(String reqUrl, Map<String, String> headerMap, Map<String, Object> paramMap) {
        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setConnectTimeout(60000);
            conn.setRequestMethod("POST");
            //header
            if (MapUtils.isNotEmpty(headerMap)) {
                Set<Map.Entry<String, String>> set = headerMap.entrySet();
                for (Map.Entry<String, String> entry : set) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String valStr = URLEncoder.encode(value.toString(), "utf-8");
                    conn.setRequestProperty(key, valStr);
                }
            }
            //param
            if (MapUtils.isNotEmpty(paramMap)) {
                if (os == null) {
                    conn.setDoOutput(true);
                    os = conn.getOutputStream();
                }
                Set<Map.Entry<String, Object>> set = paramMap.entrySet();
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Object> entry : set) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    String valStr = URLEncoder.encode(value.toString(), "utf-8");
                    sb.append(key + "=" + valStr + "&");
                }
                String body = sb.delete(sb.length() - 1, sb.length()).toString();
                IOUtils.write(body, os);
                os.flush();
            }
            is = conn.getInputStream();
            List<String> strList = IOUtils.readLines(is, "utf-8");
            return StringUtils.join(strList, "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
            conn.disconnect();
        }
    }

    public static List<String> reqGet(String url){
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(15000)
                .setConnectTimeout(15000)
                .build();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        httpget.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                List<String> lines = IOUtils.readLines(is);
                return lines;
            }
        } catch (Exception e) {
            throw new BizException("remote http request error!,"+e.getMessage(),e);
        } finally {
            IOUtils.closeQuietly(response);
        }
        return null;

    }

    public static void main(String[] args) {
//        String reqUrl = "http://www.baidu.com/mainTask/storeClickResult";
//        String reqUrl = "http://p.meekea.com:38003/getConfigValue";
//
////        paramJson = "json="+paramJson;
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//        List<String> a = new ArrayList<>();
//        a.add("1");
//        paramMap.put("key", "dproxyEnable");
//        String result = HttpUtils.getRpcJson(reqUrl + "?key=dproxyEnable");
//        String result = HttpUtils.getRpcJson(reqUrl,paramMap);

//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//        headers.setContentType(type);
//        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//        String paramJson  = JsonUtils.object2Json(paramMap);
//        HttpEntity<String> formEntity = new HttpEntity<String>(paramJson, headers);
//        String result = restTemplate.postForObject(reqUrl, formEntity, String.class);
////        String result = restTemplate.postForObject(reqUrl, paramMap, String.class, paramMap);

        List<String> lines = reqGet("http://prod002.yuntaohongbao.com:28083/checkServerStatus");
        System.out.println(StringUtils.join(lines,","));
    }
}
