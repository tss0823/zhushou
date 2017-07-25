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

import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.model.domain.ProxyContent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Component
@io.netty.channel.ChannelHandler.Sharable
public class HttpSnoopServerHandler extends SimpleChannelInboundHandler<Object> {

    ThreadLocal<ProxyContent> proxyTL = new ThreadLocal();

    @Autowired
    HttpSnoopServer httpSnoopServer;


//    public static BlockingQueue<ProxyContent> proxyContentStoreQueue = new LinkedBlockingQueue<>();


//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) {
////        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(0);
//        ProxyContent proxyContent = proxyTL.get();
//        if(proxyContent != null){
//            httpSnoopServer.addToExecute(ctx,proxyContent);
////            proxyContentQueue.offer(proxyContent);
////            charArrayBuffer.append(Thread.currentThread().getId()+"#"+DateUtil.getFmtyMdHmsSSSNoSymbol(proxyContent.getGmtRequest().getTime()));
//        }
////        else{
////            charArrayBuffer.append("error request");
////        }
//
////        ctx.flush();
//        //结束之后执行
//    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        //read0 之前拦截
        boolean state = true;
        if (state) {
            return super.acceptInboundMessage(msg);
        } else {
            return false;
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
//        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(0);
        System.out.printf("read0 msg="+msg);
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            //请求路径拦截业务 TODO
//            if (HttpHeaders.is100ContinueExpected(request)) {
//                send100Continue(ctx);
//            }
            ProxyContent proxyContent = new ProxyContent();
            proxyTL.set(proxyContent);
            String remoteInfo = ctx.channel().remoteAddress().toString();
            String remoteIp = remoteInfo.substring(1, remoteInfo.lastIndexOf(":"));
            int remotePort = Integer.valueOf(remoteInfo.substring(remoteInfo.lastIndexOf(":") + 1));
            proxyContent.setClientIp(remoteIp);
            proxyContent.setPort(remotePort);
            proxyContent.setUrl(request.getUri());
            proxyContent.setGmtRequest(new Date());
            try {
                URL oURL = new URL(request.getUri());
                proxyContent.setProtocol(oURL.getProtocol());
                proxyContent.setDomain(oURL.getAuthority());
                proxyContent.setPathUrl(oURL.getPath());
            } catch (MalformedURLException e) {
            }
            proxyContent.setReqMethod(request.getMethod().name());
            HttpHeaders headers = request.headers();
            Iterator<Map.Entry<String, String>> iterator = headers.iterator();
            Map<String, String> headerMap = new HashMap<>();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                headerMap.put(next.getKey(), next.getValue());
            }
            String headerJson = JsonUtils.object2Json(headerMap);
            proxyContent.setReqHeader(headerJson);

        }else if (msg instanceof LastHttpContent) {
            //请求内容业务拦截 TODO

            LastHttpContent httpContent = (LastHttpContent) msg;
            ByteBuf byteBuf = httpContent.content();
            byte[] req = new byte[byteBuf.readableBytes()];
            ByteBuf byteBufCopy = byteBuf.copy();
            byteBufCopy.readBytes(req);
            ProxyContent proxyContent = proxyTL.get();
            proxyContent.setReqData(new String(req));
//            reqByteArrayBuffer.append(req, 0, req.length);

            httpSnoopServer.addToExecute(ctx,proxyContent);
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            super.channelRead(ctx, msg);
        } catch (Exception e) {
//            System.err.println("e" + ExceptionUtils.getPrintStackTrace(e));
            throw e;
        } finally {

//            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}