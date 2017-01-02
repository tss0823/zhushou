package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * 请求内容
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
public class ReqContentQuery extends BaseQuery {


    private String model;

    private String appName;

    private String urlLike;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUrlLike() {
        return urlLike;
    }

    public void setUrlLike(String urlLike) {
        this.urlLike = urlLike;
    }
}
