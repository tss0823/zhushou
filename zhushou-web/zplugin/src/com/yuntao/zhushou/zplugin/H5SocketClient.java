package com.yuntao.zhushou.zplugin;

import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.web.MsgResponseObject;
import org.apache.commons.io.FileUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;


public class H5SocketClient extends WebSocketClient {

    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    protected final static Logger log = org.slf4j.LoggerFactory.getLogger(ZhushouComponent.class);

    private String fixFilePath;

    public H5SocketClient(URI serverUri, Draft draft, Map<String,String> headers) {
        super(serverUri, draft,headers,0);
    }

    public H5SocketClient(String fixFilePath) throws URISyntaxException {
        super(new URI("ws://zhushou.doublefit.cn:"+9003+"/index.index?platform=user&agent=plugin&token="+CodeBuildUtils.getLoginSid()));
        this.fixFilePath = fixFilePath;
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
        try {
            MsgResponseObject msgResponseObject = JsonUtils.json2Object(message, MsgResponseObject.class);
            if(msgResponseObject != null){
                Object data = msgResponseObject.getData();
                if(msgResponseObject.getBizType().equals("deploy_script")){
                    FileUtils.write(new File(this.fixFilePath),data+"\n","utf-8",true);
                }else{
                    FileUtils.write(new File(this.fixFilePath),data+"\n","utf-8",false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception ex) {
//        System.err.println("an error occurred:" + ex);
        bisLog.error("长连接出错了",ex);
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