package com.yuntao.zhushou.common.web.ws;

import java.util.List;

/**
 * Created by tangshengshan on 17-1-3.
 */
public class AppObject {

    private String name;

    private Integer port;

    private List<HostObject> hostObjectList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HostObject> getHostObjectList() {
        return hostObjectList;
    }

    public void setHostObjectList(List<HostObject> hostObjectList) {
        this.hostObjectList = hostObjectList;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
