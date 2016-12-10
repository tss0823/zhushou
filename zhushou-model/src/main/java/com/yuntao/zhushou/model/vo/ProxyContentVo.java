package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.param.DataMap;

import java.util.ArrayList;
import java.util.List;

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

    private List<DataMap> reqHeaderList = new ArrayList<>();

    private List<DataMap> resHeaderList = new ArrayList<>();

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

    public List<DataMap> getReqHeaderList() {
        return reqHeaderList;
    }

    public void setReqHeaderList(List<DataMap> reqHeaderList) {
        this.reqHeaderList = reqHeaderList;
    }

    public List<DataMap> getResHeaderList() {
        return resHeaderList;
    }

    public void setResHeaderList(List<DataMap> resHeaderList) {
        this.resHeaderList = resHeaderList;
    }
}
