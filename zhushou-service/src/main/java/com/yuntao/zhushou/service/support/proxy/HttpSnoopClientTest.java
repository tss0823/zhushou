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

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A simple HTTP client that prints out the content of the HTTP response to
 * {@link System#out} to test {@link HttpSnoopServer}.
 */
public final class HttpSnoopClientTest {

    static final String URL = System.getProperty("url", "http://127.0.0.1:8888/");

    static List<String> urlList = new ArrayList<>(20);
    static List<Map<String,String>> dataList = new ArrayList<>(20);

    public static void main(String[] args) throws Exception {
        System.setProperty("proxySet", "true");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "8888");

        //init
        urlList.add("http://user.api.usefullc.cn/user/getUserData");
        urlList.add("http://user.api.usefullc.cn/fileRes/token");
        urlList.add("http://user.api.usefullc.cn/user/getLoginUser");
        urlList.add("http://user.api.usefullc.cn/activeTask/start");

        //data
        Map<String,String> dataMap = new HashMap<>();
        dataMap.put("id","123456");
        dataMap.put("name","test1");
        dataList.add(dataMap);

        dataMap = new HashMap<>();
        dataMap.put("no","654321");
        dataMap.put("age","18");
        dataList.add(dataMap);
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
                        MyTestThread myThread = (MyTestThread) r;
                        Long startTime = myThread.getStartTime();
                        int index = myThread.getIndex();
                        System.out.println(Thread.currentThread().getName()+"finish! take time="+(System.currentTimeMillis()-startTime)+",index="+index);
                        System.out.println("url="+myThread.getUrl()+",bodyText="+myThread.getBodyText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            for (int i = 0; i < 1; i++) {
//                final int index = i;
//                Runnable task = new Runnable(new Thread());
                int urlSize = urlList.size();
                int urlIndex = RandomUtils.nextInt(0, urlSize);
                String url = urlList.get(urlIndex);
                int dataSize = dataList.size();
                Map<String,String> paramMap = new HashMap<>();
                if(dataSize >= urlIndex){
                    int dataIndex = RandomUtils.nextInt(0, dataSize);
                    paramMap = dataList.get(dataIndex);
                }
                url = "http://user.api.usefullc.cn/user/getLoginUser";
//                url = "http://tool.chinaz.com/map.aspx";
                MyTestThread myTestThread = new MyTestThread(url, paramMap);
                myTestThread.setIndex(i);
                ec.execute(myTestThread);
            }


            ec.shutdown();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}