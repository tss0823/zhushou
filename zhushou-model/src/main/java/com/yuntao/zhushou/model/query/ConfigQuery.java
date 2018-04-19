package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * Created by shan on 2016/3/27.
 */
public class ConfigQuery extends BaseQuery {

    private Integer type;

    private Long projectId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
