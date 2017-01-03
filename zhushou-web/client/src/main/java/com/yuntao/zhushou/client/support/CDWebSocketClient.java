package com.yuntao.zhushou.client.support;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;


@Component("webSocketClient")
public class CDWebSocketClient  extends WebSocketClient {

    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    private final static Logger log = org.slf4j.LoggerFactory.getLogger(CDWebSocketClient.class);

    @Autowired
    private CDWebSocketMsgHandler cdWebSocketMsgHandler;

    public CDWebSocketClient(URI serverUri, Draft draft, Map<String,String> headers) {
        super(serverUri, draft,headers,0);
    }

    @Autowired
    public CDWebSocketClient(@Value("${host}") String host, @Value("${port}") Integer port, @Value("${key}") String token) throws URISyntaxException {
        super(new URI("ws://"+host+":"+port+"/index.html?platform=core&token="+token));
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
//        System.out.println("new connection opened");
//        handshakedata.getHttpStatusMessage()
        bisLog.info("创建链接,"+handshakedata.getHttpStatusMessage());

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
//        System.out.println("closed with exit code " + code + " additional info: " + reason);
        //关闭重连
//        this.connect();
        bisLog.info("链接关闭,code="+code+",reason="+reason);
    }

    @Override
    public void onMessage(String message) {
//        System.out.println("received message: " + message);
        bisLog.info("收到消息"+message);
        cdWebSocketMsgHandler.onMessage(message);
    }

    @Override
    public void onError(Exception ex) {
//        System.err.println("an error occurred:" + ex);
        log.error("长连接出错了",ex);
    }

    public static void main(String[] args) throws URISyntaxException {
//        Long sendUserId = 16338L;
//        String sendUserName = "kaka";
//        Long receiveUserId = 17315L;
//        String receiveUserName = "wanmei";
//        Map<String,String> headerMap = new HashMap<>();
//        headerMap.put("cookie","token=14242314");

    }

}