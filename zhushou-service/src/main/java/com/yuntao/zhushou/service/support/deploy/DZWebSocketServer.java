package com.yuntao.zhushou.service.support.deploy;

import com.yuntao.zhushou.common.utils.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

@Component
public class DZWebSocketServer extends WebSocketServer  {

    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    private final static Logger log = org.slf4j.LoggerFactory.getLogger(DZWebSocketServer.class);

    @Autowired
    private DZMessageHelperServer ftMessageHelperServer;

    @Autowired
    public DZWebSocketServer(@Value("${webSocket.port}") int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
//        long time = new Date().getTime();
        String path = handshake.getResourceDescriptor();
        String id = conn.getRemoteSocketAddress().toString();
        DZWebSocket ytWebSocket = new DZWebSocket(id, conn);
//        taskLog.info("new webSocket connection to " + conn.getRemoteSocketAddress() + ",path=" + path);
        //权限校验 TODO

        String remoteInfo = conn.getRemoteSocketAddress().toString();
        String remoteIp = remoteInfo.substring(1, remoteInfo.lastIndexOf(":"));
        int remotePort = Integer.valueOf(remoteInfo.substring(remoteInfo.lastIndexOf(":")+1));

        String forwardIp =  handshake.getFieldValue("x-forwarded-for");
        String realIp =  handshake.getFieldValue("x-real-ip");
        if(StringUtils.isNotEmpty(forwardIp)){
            remoteIp = forwardIp;
        }else if(StringUtils.isNotEmpty(realIp)){
            remoteIp = realIp;
        }
        ytWebSocket.setRemoteIp(remoteIp);
        ytWebSocket.setRemotePort(remotePort);
        ytWebSocket.setKey(remoteIp+":"+remotePort);
        ytWebSocket.setPath(path);
        bisLog.info("创建链接,"+ytWebSocket.toString());
        try{
            ftMessageHelperServer.open(ytWebSocket, path);
        }catch (Exception e){
            log.error("链接出错,"+ExceptionUtils.getPrintStackTrace(e));
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
//        taskLog.info("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
        String id = conn.getRemoteSocketAddress().toString();
        DZWebSocket ytWebSocket = ftMessageHelperServer.getSysWebSocketMap().get(id);
        bisLog.info("关闭链接,"+ytWebSocket.toString());
        //TODO
        ftMessageHelperServer.onClose(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        String id = conn.getRemoteSocketAddress().toString();
        DZWebSocket ytWebSocket = ftMessageHelperServer.getSysWebSocketMap().get(id);
        bisLog.info("长连接收到消息,"+ytWebSocket.toString()+", message="+message);
        ftMessageHelperServer.onMessage(conn, message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        try{
            String id = conn.getRemoteSocketAddress().toString();
            DZWebSocket ytWebSocket = ftMessageHelperServer.getSysWebSocketMap().get(id);
            String desMsg = id;
            if (ytWebSocket != null) {
                desMsg = "ws="+ytWebSocket.toString()+"\r\n"+ExceptionUtils.getPrintStackTrace(ex);
            }
            bisLog.info("长连接系统错误 "+ytWebSocket.toString());
            log.error("长连接系统错误 "+ytWebSocket.toString()+",desc="+desMsg);
        }catch (Exception e){
           log.error("ws onError call failed!",e);
        }
    }


//    @Override
//    public void onWebsocketPing(WebSocket conn, Framedata f) {
//        super.onWebsocketPing(conn, f);
//        String id = conn.getRemoteSocketAddress().toString();
//        YTWebSocket ytWebSocket = messageHelperService.getSysWebSocketMap().get(id);
//        ytWebSocket.getWebSocket().sendFrame(f);
//        HbLogContextMgr.writeSuccesMsg("im","msg",ytWebSocket.getKey(),"ping收到消息",f.toString());
//    }

//    @Override
//    public void onWebsocketPong(WebSocket conn, Framedata f) {
//        String id = conn.getRemoteSocketAddress().toString();
//        FTWebSocket ytWebSocket = messageHelperService.getSysWebSocketMap().get(id);
//        ytWebSocket.getWebSocket().sendFrame(f);
//        try {
//            String data = Charsetfunctions.stringUtf8(f.getPayloadData());
//            HbLogContextMgr.writeSuccesMsg(MsgConstant.longMsg,"msg",ytWebSocket.getKey(),"pong收到正确消息",data);
//        } catch (InvalidDataException e) {
//            e.printStackTrace();
//            HbLogContextMgr.writeSuccesMsg(MsgConstant.longMsg,"msg",ytWebSocket.getKey(),"pong收到错误消息",f.toString());
//        }
//
//    }

    public static void main(String[] args) {
//        int port = 8887;
//        WebSocketServer server = new YTWebSocketServer(port);
//        server.run();
//
        List<NameValuePair> list = URLEncodedUtils.parse("name=aaa&pwd=bb", Charset.defaultCharset());
        System.out.println(list);
    }
}