/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.yuntao.zhushou.service.support.proxy;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A simple HTTP client that prints out the content of the HTTP response to
 * {@link System#out} to test {@link HttpSnoopServer}.
 */
public final class HttpClientTest2 {

    static final String URL = System.getProperty("url", "http://127.0.0.1:8888/");

    static List<String> urlList = new ArrayList<>(20);

    public static void main(String[] args) throws Exception {
        System.setProperty("proxySet", "true");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "8888");

        //init
        urlList.add("http://user.api.doublefit.cn/user/getUserData");
        urlList.add("http://user.api.doublefit.cn/fileRes/token");
        urlList.add("http://user.api.doublefit.cn/user/getLoginUser");
        urlList.add("http://user.api.doublefit.cn/user/getLoginUser");
        executeMain();



    }

    private static void executeMain(){
        try{
            final long startMainTime = System.currentTimeMillis();
            ExecutorService ec = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>()){

                @Override protected void terminated() {
                    super.terminated();
                    System.out.println("finish, take time = " + (System.currentTimeMillis() - startMainTime));


                }

                @Override protected void afterExecute(Runnable r, Throwable t) {
                    super.afterExecute(r, t);
                    try {
//                        HttpExecuteThread myThread = (HttpExecuteThread) r;
//                        Long startTime = myThread.getStartTime();
//                        int index = myThread.getIndex();
                        System.out.println(Thread.currentThread().getName()+"finish! take time="+(System.currentTimeMillis()-0)+","+0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            for (int i = 0; i < 1; i++) {
                final int index = i;
                Thread task = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{

//                            RequestRes requestRes = new RequestRes();
//                            requestRes.setProxy(true);
//                            requestRes.setProxyHost("127.0.0.1");
//                            requestRes.setProxyPort(8888);
//                            requestRes.setUrl("http://user.api.doublefit.cn/user/getUserData");
//                            ResponseRes responseRes = HttpNewUtils.execute(requestRes);
//                            String bodyText = responseRes.getBodyText();
//                            System.out.println("test "+Thread.currentThread().getName()+",bodyTest="+bodyText);


                            System.setProperty("http.proxyHost", "127.0.0.1");
                            System.setProperty("http.proxyPort", "8888");
                            org.jsoup.Connection.Response response = Jsoup.connect("http://user.api.doublefit.cn/user/getUserData2").userAgent(
                                    "my agent").timeout(10000).execute();
                            String body = response.body();
                            System.out.println("test index="+index+","+Thread.currentThread().getName()+",bodyTest="+body);
                            // Prepare the HTTP request.
//                            HttpRequest request = new DefaultFullHttpRequest(
//                                    HttpVersion.HTTP_1_1, HttpMethod.POST, "http://user.api.doublefit.cn/user/getUserData");
////            request.headers().set(HttpHeaders.Names.HOST, host);
////            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
//                            request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
//
//                            // Set some example cookies.
//                            request.headers().set(
//                                    HttpHeaders.Names.COOKIE,
//                                    ClientCookieEncoder.encode(
//                                            new DefaultCookie("my-cookie", "foo"),
//                                            new DefaultCookie("another-cookie", "bar")));
//
//                            // Send the HTTP request.
//                            ch.writeAndFlush(request);
//
//                            // Wait for the server to close the connection.
//                            ch.closeFuture().sync();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
//                Runnable task = new Runnable(new Thread());
                ec.execute(task);
            }


            ec.shutdown();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}