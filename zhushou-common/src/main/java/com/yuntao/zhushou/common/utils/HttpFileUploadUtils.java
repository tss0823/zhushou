package com.yuntao.zhushou.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class HttpFileUploadUtils {

    static CloseableHttpClient httpclient = HttpClients.createDefault();

    public static void main(String args []){
        uploadYunPan("test5.txt","bbb".getBytes());

    }

    public static void uploadYunPan(String fileName,byte [] fileData){
        String result = uploadFile(fileName,fileData);
        if(StringUtils.isNotEmpty(result)){
            JSONObject jsonObject = JSON.parseObject(result);

            String tk = jsonObject.getJSONObject("data").getString("tk");
            String etk = jsonObject.getJSONObject("data").getString("etk");
            tk = tk.replace("\\","");
            etk = etk.replace("\\","");
            addFile(tk,etk);
        }

    }

    public static void addFile(String tk,String etk){
        String url = "http://c38.yunpan.360.cn/upload/addfile/";
        Map<String,String> headerMap = new HashMap();
        headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36");
        headerMap.put("Referer","http://c38.yunpan.360.cn/my?from=firstreg");
        headerMap.put("X-Requested-With","XMLHttpRequest");
        headerMap.put("Origin","http://c38.yunpan.360.cn/");
        headerMap.put("Cookie","__guid=137882464.1979771382544523500.1447989159084.8672; __huid=10QecLC4ycc8QDNmU2ZFRrzlPXwIzXSXorH08rQBB2wlc%3D; __utma=148900148.622824456.1458785027.1458785027.1458785027.1; __utmz=148900148.1458785027.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; Q=u%3Ddvlm511%26n%3D%26le%3D%26m%3DZGH5WGWOWGWOWGWOWGWOWGWOAQV2%26qid%3D2580661590%26im%3D1_t00df551a583a87f4e9%26src%3Dpcw_so_adunion%26t%3D1; T=s%3D24d5154a6a5d14c80c2979f81e4175ed%26t%3D1465045128%26lm%3D%26lf%3D4%26sk%3D9c04ed76d784c205df68a4e48b47abb4%26mt%3D1465045128%26rc%3D%26v%3D2.0%26a%3D1; token=3664634351.38.6ec9e38f.2580661590.1465816432; count=7");


        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("tk",tk);
        paramMap.put("etk",etk);
        paramMap.put("ajax","1");

        Map<String,byte []> fileMap = new HashMap();

        try {
            String result = post(url,headerMap,paramMap,fileMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String  uploadFile(String fileName,byte [] fileData){
        String url = "http://up55.yunpan.360.cn/webupload";
        Map<String,String> headerMap = new HashMap<String,String>();
        headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36");

        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("qid","2580661590");
        paramMap.put("ofmt","json");
        paramMap.put("method","Upload.web");
        paramMap.put("token","3664634351.38.6ec9e38f.2580661590.1465816432");
        paramMap.put("v","1.0.1");
        paramMap.put("tk","ad823ad090c05c07aa71ce20c3fd7fa3d4451e99T7SVOF5PY0RcXSkjbZmDyRFiF5Si6JGegbFOBhoA69qx1t16ogPndN+tMM1B3Q/Nvosb+B4ahgoQtly/ReaIyEhBYgdb4ZK0wdtj597wLvycs+TARcoHGCT7BlVu26XkjIaahzL9m6xzPq/pUKKMcYQyfVpTxcfMhX8WTIsG248=");
        paramMap.put("Upload","Submit Query");
        paramMap.put("devtype","web");
        paramMap.put("pid","ajax");
        paramMap.put("Filename",fileName);
        String path = DateUtil.getFmtYMDNoSymbol(new Date().getTime());
        paramMap.put("path",path);

        Map<String,byte []> fileMap = new HashMap();
//        String key = fileName.substring(0,fileName.indexOf("."));
        fileMap.put(fileName,fileData);

        try {
            return post(url,headerMap,paramMap,fileMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String post(String postUrl,Map<String,String> headers, Map<String, String> params,
            Map<String, byte []> files) throws ClientProtocolException,
            IOException {
        CloseableHttpResponse response = null;
        InputStream is = null;
        String results = null;

        try {

            HttpPost httppost = new HttpPost(postUrl);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    httppost.setHeader(key,headers.get(key));
                }
            }

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            if (params != null) {
                for (String key : params.keySet()) {
                    StringBody value = new StringBody(params.get(key),
                            ContentType.TEXT_PLAIN);
                    builder.addPart(key, value);
                }
            }

            if (files != null && files.size() > 0) {
                for (String key : files.keySet()) {
                    byte [] value = files.get(key);
                    ByteArrayBody byteArrayBody = new ByteArrayBody(value,key);
                    builder.addPart("file", byteArrayBody);
                }
            }

            HttpEntity reqEntity = builder.build();
            httppost.setEntity(reqEntity);

            response = httpclient.execute(httppost);
            // assertEquals(200, response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                results = writer.toString();
            }
//            System.out.println("result=");
            System.out.println(results);

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Throwable t) {
                // No-op
            }

            try {
                if (response != null) {
                    response.close();
                }
            } catch (Throwable t) {
                // No-op
            }

        }

        return results;
    }

    public static String get(String getStr) throws IOException,
            ClientProtocolException {
        CloseableHttpResponse response = null;
        InputStream is = null;
        String results = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet(getStr);
            response = httpclient.execute(httpGet);


            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                results = writer.toString();
            }

        } finally {

            try {
                if (is != null) {
                    is.close();
                }
            } catch (Throwable t) {
                // No-op
            }

            try {
                if (response != null) {
                    response.close();
                }
            } catch (Throwable t) {
                // No-op
            }

            httpclient.close();
        }

        return results;
    }

}