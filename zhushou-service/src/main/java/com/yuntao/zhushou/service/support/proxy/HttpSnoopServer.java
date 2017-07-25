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

import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.service.impl.AbstService;
import com.yuntao.zhushou.service.inter.ProxyEsService;
import com.yuntao.zhushou.service.inter.ProxyReqFilterService;
import com.yuntao.zhushou.service.inter.ProxyResRewriteService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * An HTTP server that sends back the content of the received HTTP request
 * in a pretty plaintext form.
 */
@Component
public final class HttpSnoopServer extends AbstService {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8888"));

    @Autowired
    private ProxyEsService proxyEsService;

    @Autowired
    private HttpSnoopServerInitializer httpSnoopServerInitializer;

    @Autowired
    private HttpSnoopServerHandler httpSnoopServerHandler;

    @Autowired
    private ProxyReqFilterService proxyReqFilterService;

    @Autowired
    private ProxyResRewriteService proxyResRewriteService;


    ExecutorService ec = null;

    BlockingQueue<HttpExecuteTask> taskQueue = new LinkedBlockingQueue<>();
    BlockingQueue<ProxyContent> proxyContentStoreQueue = new LinkedBlockingQueue<>();

//    @PostConstruct
    public void init(){
//        this.createExecuteRequestPool();

        this.executeTask();

        this.storeDataTask();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpSnoopServer.this.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void addToExecute(ChannelHandlerContext ctx, ProxyContent proxyContent){
//        HttpExecuteThread httpExecuteThread = new HttpExecuteThread(ctx,this,proxyContent);
//        ec.execute(httpExecuteThread);
        HttpExecuteTask httpExecuteTask = new HttpExecuteTask(ctx, proxyContent);
        try {
            System.out.println("add to taskQueue");
            this.taskQueue.put(httpExecuteTask);

//            //重写处理
//            proxyResRewriteService.rewriteProcess(httpExecuteTask);
//
////            ProxyContent proxyContent = task.getProxyContent();
//
//            //是否存储，请求过滤逻辑
//            if (proxyReqFilterService.match(proxyContent)) {
//                HttpSnoopServer.this.proxyContentStoreQueue.put(proxyContent);
//            }

//            proxyContent = httpExecuteTask.execute();
//            HttpSnoopServer.this.proxyContentStoreQueue.put(proxyContent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



    public  void start() throws Exception{
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(httpSnoopServerInitializer);

            Channel ch = b.bind(PORT).sync().channel();

            System.err.println("Open your web browser and navigate to " +
                    (SSL? "https" : "http") + "://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void createExecuteRequestPool(){
        try{
            final long startMainTime = System.currentTimeMillis();
            ec = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>()){

                @Override protected void terminated() {
                    super.terminated();
                    log.info("finish, take time = " + (System.currentTimeMillis() - startMainTime));
                }

                @Override protected void afterExecute(Runnable r, Throwable t) {
                    super.afterExecute(r, t);
                    try {
//                        MyThread myThread = (MyThread) r;
//                        Long startTime = myThread.getStartTime();
//                        int index = myThread.getIndex();
                        HttpExecuteThread myThread = (HttpExecuteThread) r;
                        Long startTime = myThread.getStartTime();
                        log.info(Thread.currentThread().getName()+"finish! take time="+(System.currentTimeMillis()-startTime)+","+0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while(true){
//                        try {
//                            ProxyContent proxyContent = httpSnoopServerHandler.proxyContentQueue.take();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//            }).start();
//            ec.shutdown();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void executeTask() {
//        List<ProxyContent> dataList = new ArrayList<>();
//        int maxSize = 50;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        HttpExecuteTask task = HttpSnoopServer.this.taskQueue.take();

                        //重写处理
                        try{
                            proxyResRewriteService.rewriteProcess(task);
                        }finally {
                            task.execute();
                            task.writeToClient();
                        }

                        ProxyContent proxyContent = task.getProxyContent();

                        //是否存储，请求过滤逻辑
                        if (proxyReqFilterService.match(proxyContent)) {
                            HttpSnoopServer.this.proxyContentStoreQueue.put(proxyContent);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
//        while (true){
//            try{
//                ProxyContent proxyContent = proxyContentQueue.poll();
//                if(proxyContent == null)   {  //没有消息，歇一会儿
//                    Thread.sleep(1000);
//                    continue;
//                }else{
//                    proxyEsService.addPorxyContent(proxyContent);
//                }
//            }catch (Exception e){
//                bisLog.error("storeDataTask failed! ",e);
//            }
//        }

    }

    private void storeDataTask() {
//        List<ProxyContent> dataList = new ArrayList<>();
//        int maxSize = 50;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        ProxyContent proxyContent = HttpSnoopServer.this.proxyContentStoreQueue.take();
                        proxyEsService.addPorxyContent(proxyContent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
//        while (true){
//            try{
//                ProxyContent proxyContent = proxyContentQueue.poll();
//                if(proxyContent == null)   {  //没有消息，歇一会儿
//                    Thread.sleep(1000);
//                    continue;
//                }else{
//                    proxyEsService.addPorxyContent(proxyContent);
//                }
//            }catch (Exception e){
//                bisLog.error("storeDataTask failed! ",e);
//            }
//        }

    }

    public static void main(String[] args) throws Exception {
//        start();
    }

}