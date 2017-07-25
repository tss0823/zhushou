package com.yuntao.zhushou.service.support.proxy;

import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.ExceptionUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.enums.ProxyContentStatus;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Date;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by shan on 2017/7/19.
 */
public class HttpExecuteThread extends Thread {

    private Long startTime;

    private ProxyContent proxyContent;

    private HttpSnoopServer httpSnoopServer;

    private ChannelHandlerContext ctx;

    public HttpExecuteThread(ChannelHandlerContext ctx,HttpSnoopServer httpSnoopServer,ProxyContent proxyContent) {
        this.ctx = ctx;
        this.httpSnoopServer = httpSnoopServer;
        this.proxyContent = proxyContent;
    }

    @Override
    public void run() {
        this.startTime = System.currentTimeMillis();
        String url = proxyContent.getUrl();
        String reqMethod = proxyContent.getReqMethod();
        try {
            ResponseRes responseRes = null;
            if (reqMethod.equalsIgnoreCase("get")) {
                responseRes = HttpNewUtils.get(url);
            } else {
                RequestRes requestRes = new RequestRes();
                requestRes.setUrl(url);
                String reqData = proxyContent.getReqData();
                requestRes.setParamText(reqData);
                responseRes = HttpNewUtils.execute(requestRes);
            }
            proxyContent.setResData(responseRes.getBodyText());
            proxyContent.setResLength(responseRes.getResult().length);
            proxyContent.setHttpStatus(responseRes.getStatus());
            proxyContent.setStatus(ProxyContentStatus.success.getCode());
            proxyContent.setGmtResponse(new Date());
            Map<String, String> headersMap = responseRes.getHeaders();
            String headerJson = JsonUtils.object2Json(headersMap);
            proxyContent.setResHeader(headerJson);

            String contentType = headersMap.get("Content-Type");
            proxyContent.setResContentType(contentType);

        } catch (Exception e) {
            proxyContent.setResData(ExceptionUtils.getPrintStackTrace(e));
            proxyContent.setHttpStatus(500);
            proxyContent.setStatus(ProxyContentStatus.error.getCode());
        }

        //to client for response
        FullHttpResponse backResponse = new DefaultFullHttpResponse(
                HTTP_1_1, proxyContent.getStatus() == ProxyContentStatus.success.getCode() ? OK : BAD_REQUEST,
                Unpooled.wrappedBuffer(proxyContent.getResData().getBytes()));
        ctx.writeAndFlush(backResponse).addListener(ChannelFutureListener.CLOSE);

        //to store query
        httpSnoopServer.proxyContentStoreQueue.offer(proxyContent);

    }

    public Long getStartTime() {
        return startTime;
    }
}
