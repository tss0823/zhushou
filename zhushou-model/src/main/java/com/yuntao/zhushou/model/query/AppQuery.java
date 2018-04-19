package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * Created by shan on 2016/3/27.
 */
public class AppQuery  extends BaseQuery {

    private Long id;

    private Long projectId;

    private String name;

    private Integer front;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFront() {
        return front;
    }

    public void setFront(Integer front) {
        this.front = front;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
