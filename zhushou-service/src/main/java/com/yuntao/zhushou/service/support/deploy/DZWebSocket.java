package com.yuntao.zhushou.service.support.deploy;

import org.apache.commons.lang3.StringUtils;
import org.java_websocket.WebSocket;

import java.io.Serializable;

/**
 * Created by shan on 2016/5/15.
 */
public class DZWebSocket implements Serializable {

    private String id;

    private String key;

    private String remoteIp;

    private int remotePort;

    private String localIp;

    private int localPort;

    private String path;

    private String cpKey;  //company key

    private String platform; //平台类型 user,core

    private Long userId;  //user id for user


    /**
     * 授权状态，0：未授权；1：已授权
     */
    private Integer authStatus = 0;

    /**
     * 逻辑连接状态  0 下线 1 在线
     */
    private Integer status = 0;

    private WebSocket webSocket;

    public DZWebSocket(String id, WebSocket webSocket) {
        this.id = id;
        this.webSocket = webSocket;
        this.status = 1;
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

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "key="+key+",path="+path;
    }

    @Override
    public boolean equals(Object obj) {
        return StringUtils.equals(toString(),obj.toString());
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCpKey() {
        return cpKey;
    }

    public void setCpKey(String cpKey) {
        this.cpKey = cpKey;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
