package com.yuntao.zhushou.common.web.ws;

/**
 * Created by tangshengshan on 17-1-3.
 */
public class HostObject {

    private String name;

    private String host;


    /**
     * 检测状态
     */
    private boolean success;

    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
