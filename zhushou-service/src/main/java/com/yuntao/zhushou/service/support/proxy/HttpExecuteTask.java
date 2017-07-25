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
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by shan on 2017/7/19.
 */
public class HttpExecuteTask extends Thread {


    private ProxyContent proxyContent;


    private ChannelHandlerContext ctx;

    private boolean execute;

    private boolean writeToClient;

    public HttpExecuteTask(ChannelHandlerContext ctx, ProxyContent proxyContent) {
        this.ctx = ctx;
        this.proxyContent = proxyContent;
    }

    public ProxyContent execute(){
        if(execute){
            return proxyContent;
        }
        execute = true;
        String url = proxyContent.getUrl();
        String reqMethod = proxyContent.getReqMethod();
        try {
            ResponseRes responseRes = null;
            if (reqMethod.equalsIgnoreCase("get")) {
                responseRes = HttpNewUtils.get(url);
            } else {
                RequestRes requestRes = new RequestRes();
                String reqHeader = proxyContent.getReqHeader();
                Map<String,String> reqHeaderMap = JsonUtils.json2Object(reqHeader, Map.class);
                requestRes.setUrl(url);
                requestRes.setHeaders(reqHeaderMap);

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

        return proxyContent;
    }

    public void writeToClient(){
        if(writeToClient){
            return;
        }
        writeToClient = true;

        //to client for response
        HttpResponseStatus status = HttpResponseStatus.valueOf(proxyContent.getHttpStatus());
        FullHttpResponse backResponse = new DefaultFullHttpResponse(
                HTTP_1_1, status,
                Unpooled.wrappedBuffer(proxyContent.getResData().getBytes()));

        //add headers
        String resHeader = proxyContent.getResHeader();
        if(StringUtils.isNotEmpty(resHeader)){
            Map<String,String> resHeaderMap = JsonUtils.json2Object(resHeader, Map.class);
            Set<Map.Entry<String, String>> entries = resHeaderMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                backResponse.headers().set(entry.getKey(),entry.getValue());
            }
        }else{
            System.out.println(11);
        }

        ctx.writeAndFlush(backResponse).addListener(ChannelFutureListener.CLOSE);

    }

    public ProxyContent getProxyContent() {
        return proxyContent;
    }
}
