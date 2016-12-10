package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.ProxyContent;

/**
 * 请求内容
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
public class ProxyContentVo extends ProxyContent {

    private String statusText;

    private String lastReqTime;

    private String lastResTime;

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getLastReqTime() {
        return lastReqTime;
    }

    public void setLastReqTime(String lastReqTime) {
        this.lastReqTime = lastReqTime;
    }

    public String getLastResTime() {
        return lastResTime;
    }

    public void setLastResTime(String lastResTime) {
        this.lastResTime = lastResTime;
    }
}
