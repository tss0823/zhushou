package com.yuntao.zhushou.service.support.bis;

import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.enums.ProxyContentStatus;
import com.yuntao.zhushou.service.impl.AbstService;
import com.yuntao.zhushou.service.inter.ProxyContentService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.http.util.ByteArrayBuffer;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tangshengshan on 16-12-10.
 */
@Component
public class HttpProxyServerSupport extends AbstService {


    private Queue<ProxyContent> proxyContentQueue = new ConcurrentLinkedQueue<>();

    @Autowired
    private ProxyContentService proxyContentService;


    @PostConstruct
    private void init() {
        serverStart();

        ExecutorService ec = Executors.newFixedThreadPool(1);
        ec.execute(new Runnable() {
            @Override
            public void run() {
                storeDataTask();
            }
        });
    }

    private void serverStart() {
        HttpProxyServer server =
                DefaultHttpProxyServer.bootstrap()
                        .withAddress(new InetSocketAddress("0.0.0.0",8888))
//                        .withPort(8888)
                        .withFiltersSource(new HttpFiltersSourceAdapter() {

                            //                            @Override
//                            public int getMaximumResponseBufferSizeInBytes() {
//                                return 10 * 1024;
//                            }
                            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                                final ProxyContent proxyContent = new ProxyContent();
                                String remoteInfo = ctx.channel().remoteAddress().toString();
                                String remoteIp = remoteInfo.substring(1, remoteInfo.lastIndexOf(":"));
                                int remotePort = Integer.valueOf(remoteInfo.substring(remoteInfo.lastIndexOf(":") + 1));
                                proxyContent.setClientIp(remoteIp);
                                proxyContent.setPort(remotePort);
//                                reqMap.put(sid,new ProxyContent());
                                final ByteArrayBuffer reqByteArrayBuffer = new ByteArrayBuffer(0);
                                final ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(0);

                                return new HttpFiltersAdapter(originalRequest) {
                                    @Override
                                    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                                        // TODO: implement your filtering here
                                        System.err.println("clientToProxyRequest httpObject=" + httpObject);
                                        if(proxyContent.getGmtRequest() == null){
                                            proxyContent.setGmtRequest(new Date());
                                        }
                                        if (httpObject instanceof DefaultHttpRequest) {
                                            DefaultHttpRequest defaultHttpRequest = (DefaultHttpRequest) httpObject;
                                            String uri = defaultHttpRequest.getUri();
                                            proxyContent.setUrl(uri);
                                            try {
                                                URL oURL = new URL(uri);
                                                proxyContent.setDomain(oURL.getAuthority());
                                                proxyContent.setPathUrl(oURL.getPath());
                                            } catch (MalformedURLException e) {
                                            }
                                            proxyContent.setReqMethod(defaultHttpRequest.getMethod().name());
                                            HttpHeaders headers = defaultHttpRequest.headers();
                                            Iterator<Map.Entry<String, String>> iterator = headers.iterator();
                                            Map<String, String> headerMap = new HashMap<>();
                                            while (iterator.hasNext()) {
                                                Map.Entry<String, String> next = iterator.next();
                                                headerMap.put(next.getKey(), next.getValue());
                                            }
                                            String headerJson = JsonUtils.object2Json(headerMap);
                                            proxyContent.setReqHeader(headerJson);
                                        }
                                        if (httpObject instanceof DefaultHttpContent) {
                                            //
                                            DefaultHttpContent defaultHttpContent = (DefaultHttpContent) httpObject;
                                            ByteBuf byteBuf = defaultHttpContent.content();
                                            byte[] req = new byte[byteBuf.readableBytes()];
                                            ByteBuf byteBufCopy = byteBuf.copy();
                                            byteBufCopy.readBytes(req);
                                            reqByteArrayBuffer.append(req, 0, req.length);
                                        }
                                        return null;
                                    }

                                    @Override
                                    public HttpObject serverToProxyResponse(HttpObject httpObject) {
                                        // TODO: implement your filtering here
                                        System.err.println("serverToProxyResponse httpObject=" + httpObject);
                                        if(proxyContent.getGmtResponse() == null){
                                            proxyContent.setGmtResponse(new Date());
                                        }
                                        if (httpObject instanceof DefaultHttpResponse) {
                                            //
                                            DefaultHttpResponse defaultHttpResponse = (DefaultHttpResponse) httpObject;
                                            int httpCode = defaultHttpResponse.getStatus().code();
                                            proxyContent.setHttpStatus(httpCode);


                                            HttpHeaders headers = defaultHttpResponse.headers();
                                            String contentType = headers.get("Content-Type");
                                            proxyContent.setResContentType(contentType);
                                            Iterator<Map.Entry<String, String>> iterator = headers.iterator();
                                            Map<String, String> headerMap = new HashMap<>();
                                            while (iterator.hasNext()) {
                                                Map.Entry<String, String> next = iterator.next();
                                                headerMap.put(next.getKey(), next.getValue());
                                            }
                                            String headerJson = JsonUtils.object2Json(headerMap);
                                            proxyContent.setResHeader(headerJson);
                                        }

                                        if (httpObject instanceof DefaultHttpContent) {
                                            //
                                            DefaultHttpContent defaultHttpContent = (DefaultHttpContent) httpObject;
                                            ByteBuf byteBuf = defaultHttpContent.content();
                                            byte[] req = new byte[byteBuf.readableBytes()];
                                            ByteBuf byteBufCopy = byteBuf.copy();
                                            byteBufCopy.readBytes(req);
                                            byteArrayBuffer.append(req, 0, req.length);
                                        }
                                        return httpObject;
                                    }

                                    @Override
                                    public void serverToProxyResponseTimedOut() {
                                        System.err.println("serverToProxyResponseTimedOut");
                                        super.serverToProxyResponseTimedOut();

                                        //store
                                        int status = ProxyContentStatus.timeout.getCode();
                                        HttpProxyServerSupport.this.offerData(proxyContent, status, reqByteArrayBuffer, byteArrayBuffer);
                                    }


                                    @Override
                                    public void serverToProxyResponseReceived() {
                                        //成功发送数据,结束
                                        super.serverToProxyResponseReceived();
                                        System.err.println("serverToProxyResponseReceived");

                                        //store
                                        int status = ProxyContentStatus.success.getCode();
                                        HttpProxyServerSupport.this.offerData(proxyContent, status, reqByteArrayBuffer, byteArrayBuffer);

                                    }


                                    @Override
                                    public void proxyToServerResolutionFailed(String hostAndPort) {
                                        super.proxyToServerResolutionFailed(hostAndPort);
                                        //502 服务error
                                        System.err.println("proxyToServerResolutionFailed");

                                        //store
                                        int status = ProxyContentStatus.error.getCode();
                                        HttpProxyServerSupport.this.offerData(proxyContent, status, reqByteArrayBuffer, byteArrayBuffer);

                                    }


                                };
                            }
                        })
                        .start();


    }

    private void offerData(ProxyContent proxyContent, Integer status, ByteArrayBuffer reqByteArrayBuffer, ByteArrayBuffer resByteArrayBuffer){
        //reqData
        if(reqByteArrayBuffer.length() > 0){
            byte[] buffer = reqByteArrayBuffer.buffer();
//            String content = new String(buffer);
            proxyContent.setReqData(buffer);
        }

        //resData
        if(resByteArrayBuffer.length() > 0){
            byte[] buffer = resByteArrayBuffer.buffer();
//            String content = new String(buffer);
            proxyContent.setResData(buffer);
            proxyContent.setResLength(buffer.length);
        }
        proxyContent.setStatus(status);

        //添加到队列
        proxyContentQueue.offer(proxyContent);
    }

    private void storeDataTask() {
        List<ProxyContent> dataList = new ArrayList<>();
        int maxSize = 200;
        try{
            while (true){
                ProxyContent proxyContent = proxyContentQueue.poll();
                if(proxyContent == null)   {  //没有消息，歇一会儿
                    if(dataList.size() == 0){
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                        }
                    }else{
                        proxyContentService.insertBatch(dataList);
                        dataList.clear();
                    }
                    continue;
                }
                dataList.add(proxyContent);
                if(dataList.size() >= maxSize){
                    proxyContentService.insertBatch(dataList);
                    dataList.clear();
                }
            }
        }catch (Exception e){
            bisLog.error("storeDataTask failed! ",e);
        }

    }
}
