package com.yuntao.zhushou.client.support;

import com.yuntao.zhushou.common.constant.CacheConstant;
import com.yuntao.zhushou.common.constant.MsgConstant;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.utils.SystemUtils;
import com.yuntao.zhushou.common.web.MsgRequestObject;
import com.yuntao.zhushou.common.web.MsgResponseObject;
import com.yuntao.zhushou.common.web.ResponseObject;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * Created by tangshengshan on 17-1-1.
 */
@Component
public class CDWebSocketMsgHandler implements InitializingBean {

    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    private final static Logger log = org.slf4j.LoggerFactory.getLogger(CDWebSocketMsgHandler.class);

    @Value("${key}")
    private String key;

    @Value("${host}")
    private String host;

    @Value("${port}")
    private Integer port;

    @Value("${http.host}")
    private String httpHost;

    @Value("${http.port}")
    private String httpPort;

    private volatile boolean isFirstConn = true; //第一次链接

    @Autowired
    private WebSocketClient webSocketClient;

//    public void open(){
//    }


    public void offerMsg(String bizType, String message) {
        MsgRequestObject requestObject = new MsgRequestObject();
        requestObject.setType(MsgConstant.ReqResType.CORE);
        requestObject.setBizType(bizType);
        requestObject.setMessage(message);
        String json = JsonUtils.object2Json(requestObject);
        if (webSocketClient.getConnection().getReadyState().name().equals(WebSocket.READYSTATE.OPEN.name())) {
            webSocketClient.send(json);
        } else {
            bisLog.info("发送消息失败,链接已关闭");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        String url = "ws://"+host+":"+port+"/index.html?token="+key;
//        webSocketClient = new CDWebSocketClient(host,port,key);
//        try{
//
//            webSocketClient.connect();
//
//
//        }catch (Exception e){
//
//        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (webSocketClient.getConnection().getReadyState().name().equals(WebSocket.READYSTATE.CLOSED.name()) ||
                                webSocketClient.getConnection().getReadyState().name().equals(WebSocket.READYSTATE.NOT_YET_CONNECTED.name())) {
                            webSocketClient.connect();
                            if (isFirstConn) {
                                //提交 http host and port
                                CDWebSocketMsgHandler.this.offerMsg(MsgConstant.ReqCoreBizType.UPDATE_ADDRESS, httpHost + ":" + httpPort);
                                isFirstConn = false;
                            }
                        }
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
