package com.yuntao.zhushou.service.support;

import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

//@Component
public class YTWebSocketServer extends WebSocketServer implements InitializingBean {



    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    private final static Logger log = org.slf4j.LoggerFactory.getLogger(YTWebSocketServer.class);

    private List<YTWebSocket> ytWebSocketList  = new LinkedList<>();

    @Autowired
    public YTWebSocketServer(@Value("${webSocket.port}") int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String path = handshake.getResourceDescriptor();
        bisLog.info("new webSocket connection to "+conn.getRemoteSocketAddress()+",path="+path);
        YTWebSocket ytWebSocket = new YTWebSocket(path,conn);
        ytWebSocketList.add(ytWebSocket);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        bisLog.info("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
        for(YTWebSocket ytWebSocket : ytWebSocketList){
            WebSocket webSocket  = ytWebSocket.getWebSocket();
            if(conn.toString().equals(webSocket.toString())){
                ytWebSocketList.remove(ytWebSocket);
                break;
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        bisLog.info("received message from " + conn.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        bisLog.error("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }

    public static void main(String[] args) {
        int port = 8887;
        WebSocketServer server = new YTWebSocketServer(port);
        server.run();

    }

    public void sendMessage(String type,String message){
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setType(type);
        responseObject.setData(message);
        String jsonMsg = JsonUtils.object2Json(responseObject);
       for(YTWebSocket ytWebSocket : ytWebSocketList) {
           WebSocket webSocket = ytWebSocket.getWebSocket();
           if(!webSocket.getReadyState().name().equals(WebSocket.READYSTATE.OPEN.name())){
               continue;
           }
           webSocket.send(jsonMsg);
       }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                YTWebSocketServer.this.run();
            }
        }).start();
    }
}