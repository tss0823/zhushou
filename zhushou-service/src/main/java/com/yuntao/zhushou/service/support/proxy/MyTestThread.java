package com.yuntao.zhushou.service.support.proxy;

import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shan on 2017/7/20.
 */
public class MyTestThread extends Thread {

    private int index;

    private String url;

    private Long startTime;

    private String bodyText;

    private Map<String, String> paramMap;

    public MyTestThread(String url, Map<String, String> paramMap) {
        this.url = url;
        this.paramMap = paramMap;
    }

    @Override
    public void run() {
        try {
            startTime = System.currentTimeMillis();
            System.setProperty("http.proxyHost", "127.0.0.1");
            System.setProperty("http.proxyPort", "8888");


//                            RequestRes requestRes = new RequestRes();
//                            requestRes.setProxy(true);
//                            requestRes.setProxyHost("127.0.0.1");
//                            requestRes.setProxyPort(8888);
//                            requestRes.setUrl("http://user.api.doublefit.cn/user/getUserData");
//                            ResponseRes responseRes = HttpNewUtils.execute(requestRes);
//                            String bodyText = responseRes.getBodyText();
//                            System.out.println("test "+Thread.currentThread().getName()+",bodyTest="+bodyText);

            String ch = RandomStringUtils.random(1, "yn");
            boolean isPostMethod = BooleanUtils.toBoolean(ch);
            Connection.Method method = Connection.Method.POST;
            if(!isPostMethod){
                method = Connection.Method.GET;
            }
            RequestRes requestRes = new RequestRes();
            requestRes.setUrl(url);
            requestRes.setProxy(true);
            requestRes.setProxyHost("127.0.0.1");
            requestRes.setProxyPort(8888);
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("user-agent","my agent2");
            requestRes.setHeaders(headerMap);
            ResponseRes responseRes = HttpNewUtils.execute(requestRes);
//            org.jsoup.Connection.Response response = Jsoup.connect(url).method(method).data(paramMap).userAgent(
//                    "my agent").timeout(10000).execute();
            bodyText = responseRes.getBodyText();
        } catch (Exception e) {
            e.printStackTrace();
            bodyText = e.getMessage();
            ;
        }
    }

    public String getUrl() {
        return url;
    }

    public Long getStartTime() {
        return startTime;
    }

    public String getBodyText() {
        return bodyText;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
