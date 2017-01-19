package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * 代理内容
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
public class ProxyContentQuery extends BaseQuery {


    private String clientIp;

    private String port;

    private String reqTimeStart;

    private String reqTimeEnd;

    private String month;

    private String model;

    private String id;

    private Integer status;


    private String urlType = "match";

    private String url ;

    private String clientIpType = "term";

    private String userAgentType = "fuzzy";

    private String userAgent;

    private String textCat;

    private String textType = "fuzzy";

    private String text;

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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientIpType() {
        return clientIpType;
    }

    public void setClientIpType(String clientIpType) {
        this.clientIpType = clientIpType;
    }

    public String getUserAgentType() {
        return userAgentType;
    }

    public void setUserAgentType(String userAgentType) {
        this.userAgentType = userAgentType;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getTextCat() {
        return textCat;
    }

    public void setTextCat(String textCat) {
        this.textCat = textCat;
    }

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        this.textType = textType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
