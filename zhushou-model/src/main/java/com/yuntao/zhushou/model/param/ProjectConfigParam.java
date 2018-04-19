package com.yuntao.zhushou.model.param;

import com.yuntao.zhushou.model.domain.Config;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shan on 2018/4/7.
 */
public class ProjectConfigParam implements Serializable {

    private Long projectId;

    private List<Config> configList;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<Config> getConfigList() {
        return configList;
    }

    public void setConfigList(List<Config> configList) {
        this.configList = configList;
    }
}
