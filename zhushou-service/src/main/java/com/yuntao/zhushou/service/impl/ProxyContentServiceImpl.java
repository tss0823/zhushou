package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.dal.mapper.ProxyContentMapper;
import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.query.ProxyContentQuery;
import com.yuntao.zhushou.model.vo.ProxyContentVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.service.inter.ProxyContentService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.ByteArrayBuffer;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 请求内容服务实现类
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
@Service("proxyContentService")
public class ProxyContentServiceImpl implements ProxyContentService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProxyContentMapper reqContentMapper;

    int reqSize = 800;

    //cache 使用LRU 策略
    Map<String,Object> reqMap = new LinkedHashMap(reqSize){
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return this.size() >= reqSize;
        }
    };

    {
        reqMap = new ConcurrentHashMap<>(1000);
    }

    @PostConstruct
    public void init() {
        HttpProxyServer server =
                DefaultHttpProxyServer.bootstrap()
                        .withPort(8888)
                        .withFiltersSource(new HttpFiltersSourceAdapter() {

                            //                            @Override
//                            public int getMaximumResponseBufferSizeInBytes() {
//                                return 10 * 1024;
//                            }
                            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                                final ProxyContent proxyContent = new ProxyContent();
                                String remoteInfo = ctx.channel().remoteAddress().toString();
                                String remoteIp = remoteInfo.substring(1, remoteInfo.lastIndexOf(":"));
                                int remotePort = Integer.valueOf(remoteInfo.substring(remoteInfo.lastIndexOf(":")+1));
                                proxyContent.setClientIp(remoteIp);
                                proxyContent.setPort(remotePort);
//                                reqMap.put(sid,new ProxyContent());
                                final ByteArrayBuffer reqByteArrayBuffer = new ByteArrayBuffer(0);
                                final ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(0);

                                return new HttpFiltersAdapter(originalRequest) {
                                    @Override
                                    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                                        // TODO: implement your filtering here
                                        System.err.println("clientToProxyRequest httpObject="+httpObject);
                                        if(httpObject instanceof DefaultHttpRequest){
                                            DefaultHttpRequest defaultHttpRequest = (DefaultHttpRequest) httpObject;
                                            String uri = defaultHttpRequest.getUri();
                                            proxyContent.setUrl(uri);
                                            proxyContent.setReqMethod(defaultHttpRequest.getMethod().name());
                                            HttpHeaders headers = defaultHttpRequest.headers();
                                            Iterator<Map.Entry<String, String>> iterator = headers.iterator();
                                            Map<String,String> headerMap = new HashMap<>();
                                            while (iterator.hasNext()) {
                                                Map.Entry<String, String> next = iterator.next();
                                                headerMap.put(next.getKey(),next.getValue());
                                            }
                                            String headerJson = JsonUtils.object2Json(headerMap);
                                            proxyContent.setReqHeader(headerJson);
                                        }
                                        if(httpObject instanceof DefaultHttpContent){
                                            //
                                            DefaultHttpContent defaultHttpContent = (DefaultHttpContent) httpObject;
                                            ByteBuf byteBuf = defaultHttpContent.content();
                                            byte[] req = new byte[byteBuf.readableBytes()];
                                            ByteBuf byteBufCopy = byteBuf.copy();
                                            byteBufCopy.readBytes(req);
                                            reqByteArrayBuffer.append(req,0,req.length);
                                        }
                                        return null;
                                    }

                                    @Override
                                    public HttpObject serverToProxyResponse(HttpObject httpObject) {
                                        // TODO: implement your filtering here
                                        System.err.println("serverToProxyResponse httpObject="+httpObject);
                                        if(httpObject instanceof DefaultHttpResponse){
                                            //
                                            DefaultHttpResponse defaultHttpResponse = (DefaultHttpResponse) httpObject;
                                            int httpCode = defaultHttpResponse.getStatus().code();
                                            proxyContent.setHttpStatus(httpCode);

                                            HttpHeaders headers = defaultHttpResponse.headers();
                                            Iterator<Map.Entry<String, String>> iterator = headers.iterator();
                                            Map<String,String> headerMap = new HashMap<>();
                                            while (iterator.hasNext()) {
                                                Map.Entry<String, String> next = iterator.next();
                                                headerMap.put(next.getKey(),next.getValue());
                                            }
                                            String headerJson = JsonUtils.object2Json(headerMap);
                                            proxyContent.setResHeader(headerJson);
                                        }

                                        if(httpObject instanceof DefaultHttpContent){
                                            //
                                            DefaultHttpContent defaultHttpContent = (DefaultHttpContent) httpObject;
                                            ByteBuf byteBuf = defaultHttpContent.content();
                                            byte[] req = new byte[byteBuf.readableBytes()];
                                            ByteBuf byteBufCopy = byteBuf.copy();
                                            byteBufCopy.readBytes(req);
                                            byteArrayBuffer.append(req,0,req.length);
                                        }
                                        return httpObject;
                                    }

                                    @Override
                                    public void serverToProxyResponseTimedOut() {
                                        System.err.println("serverToProxyResponseTimedOut");
                                        super.serverToProxyResponseTimedOut();
                                    }


                                    @Override
                                    public void serverToProxyResponseReceived() {
                                        //成功发送数据,结束
                                        System.err.println("serverToProxyResponseReceived");
                                        byte[] buffer = byteArrayBuffer.buffer();
                                        String content = new String(buffer);
                                        proxyContent.setResData(content);
//                                        System.out.println(content);
                                        super.serverToProxyResponseReceived();

                                        //req data
                                        if(StringUtils.endsWithIgnoreCase("get",proxyContent.getReqMethod())){

                                        }else if(StringUtils.endsWithIgnoreCase("post",proxyContent.getReqMethod())){
                                            byte[] reqBuffer = reqByteArrayBuffer.buffer();
                                            String reqData = new String(reqBuffer);
                                            proxyContent.setReqData(reqData);
                                        }
                                        //保存数据
                                        ProxyContentServiceImpl.this.insert(proxyContent);
                                    }




                                    @Override
                                    public void proxyToServerResolutionFailed(String hostAndPort) {
                                        //502 服务error
                                        System.err.println("proxyToServerResolutionFailed");
                                        super.proxyToServerResolutionFailed(hostAndPort);
                                        
                                        //req data
                                        if(StringUtils.endsWithIgnoreCase("get",proxyContent.getReqMethod())){

                                        }else if(StringUtils.endsWithIgnoreCase("post",proxyContent.getReqMethod())){
                                            byte[] reqBuffer = reqByteArrayBuffer.buffer();
                                            String reqData = new String(reqBuffer);
                                            proxyContent.setReqData(reqData);
                                        }

                                        //保存数据
                                        ProxyContentServiceImpl.this.insert(proxyContent);
                                    }


                                };
                            }
                        })
                        .start();

    }

    @Override
    public List<ProxyContent> selectList(ProxyContentQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return reqContentMapper.selectList(queryMap);
    }

    @Override
    public Pagination<ProxyContentVo> selectPage(ProxyContentQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = reqContentMapper.selectListCount(queryMap);
        Pagination<ProxyContentVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination",pagination);
        List<ProxyContent> dataList = reqContentMapper.selectList(queryMap);
//        Pagination<ProxyContentVo> newPageInfo = new Pagination<>(pagination);
        List<ProxyContentVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
//        pagination.setDataList(dataList);
        for(ProxyContent reqContent : dataList){
            ProxyContentVo reqContentVo = BeanUtils.beanCopy(reqContent,ProxyContentVo.class);
            newDataList.add(reqContentVo);
        }
        return pagination;

    }

    @Override
    public ProxyContent findById(Long id) {
        return reqContentMapper.findById(id);
    }


    @Override
    public int insert(ProxyContent reqContent) {
        return reqContentMapper.insert(reqContent);
    }

    @Override
    public int updateById(ProxyContent reqContent) {
        return reqContentMapper.updateById(reqContent);
    }

    @Override
    public int deleteById(Long id) {
        return reqContentMapper.deleteById(id);
    }

}
