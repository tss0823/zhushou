package com.yuntao.zhushou.model.query;

/**
 * 代理内容
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
public class ProxyContentQuery extends  BaseQuery {


    private String clientIp;

    private String port;

    private String reqTimeStart;

    private String reqTimeEnd;

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getReqTimeStart() {
        return reqTimeStart;
    }

    public void setReqTimeStart(String reqTimeStart) {
        this.reqTimeStart = reqTimeStart;
    }

    public String getReqTimeEnd() {
        return reqTimeEnd;
    }

    public void setReqTimeEnd(String reqTimeEnd) {
        this.reqTimeEnd = reqTimeEnd;
    }
}
