package com.yuntao.zhushou.model.query;

import java.io.Serializable;

/**
 * Created by shan on 2016/5/5.
 */
public class LogTextQuery implements Serializable {

    private String urlType = "match";

    private String url ;

    private String mobileType = "term";

    private String mobile;

    private String userAgentType = "fuzzy";

    private String userAgent;

    private String textCat;

    private String textType = "fuzzy";

    private String text;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public String getUserAgentType() {
        return userAgentType;
    }

    public void setUserAgentType(String userAgentType) {
        this.userAgentType = userAgentType;
    }

    public String getTextCat() {
        return textCat;
    }

    public void setTextCat(String textCat) {
        this.textCat = textCat;
    }
}
