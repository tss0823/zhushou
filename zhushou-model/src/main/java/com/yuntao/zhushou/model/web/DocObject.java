package com.yuntao.zhushou.model.web;

import java.io.Serializable;

/**
 * Created by tangshengshan on 16-12-12.
 */
public class DocObject implements Serializable {

    private String appName;

    private String url;

    private String author;

    private String comment;

    private String module;

    private String ver;

    private Object paramObj;

    private Object returnObj;

    public Object getParamObj() {
        return paramObj;
    }

    public void setParamObj(Object paramObj) {
        this.paramObj = paramObj;
    }

    public Object getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(Object returnObj) {
        this.returnObj = returnObj;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}
