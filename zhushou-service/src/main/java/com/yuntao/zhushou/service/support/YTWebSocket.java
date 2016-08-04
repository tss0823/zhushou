package com.yuntao.zhushou.service.support;

import org.java_websocket.WebSocket;

import java.io.Serializable;

/**
 * Created by shan on 2016/5/15.
 */
public class YTWebSocket implements Serializable {

    private String key;

    private WebSocket webSocket;


    public YTWebSocket( String key,WebSocket webSocket) {
        this.key = key;
        this.webSocket = webSocket;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
