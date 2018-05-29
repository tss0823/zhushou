package com.yuntao.zhushou.service.support.proxy;

import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by shan on 2017/7/21.
 */
public class HttpClientTest {

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 1; i++) {
            System.setProperty("http.proxyHost", "127.0.0.1");
            System.setProperty("http.proxyPort", "8888");

            RequestRes requestRes = new RequestRes();
            requestRes.setUrl("http://test.user.api.usefullc.cn/invite/money");
            requestRes.setProxyHost("127.0.0.1");
            requestRes.setProxy(true);
            requestRes.setProxyPort(8888);
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("cookie","sid=e64cd1f6-3593-4454-9866-8263706820f6");
            requestRes.setHeaders(headerMap);
//            requestRes.setParamText("");
            ResponseRes responseRes = HttpNewUtils.execute(requestRes);
//            org.jsoup.Connection.Response response = Jsoup.connect("http://user.api.usefullc.cn/user/getUserData").userAgent(
//                    "my agent").timeout(10000).execute();
            Map<String, String> headers = responseRes.getHeaders();
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            System.out.println("============headers result==============");
            for (Map.Entry<String, String> entry : entries) {
                System.out.println("key="+entry.getKey()+",value="+entry.getValue());
            }
            String body = responseRes.getBodyText();
            System.out.println("============body result==============");
            System.out.println(body);
        }
    }
}
