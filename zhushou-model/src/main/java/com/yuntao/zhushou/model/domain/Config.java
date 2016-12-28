package com.yuntao.zhushou.model.domain;


import java.io.Serializable;

/**
 * Created by shengshan.tang on 11/4/2015 at 4:59 PM
 */
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 企业id
     */
    private Long companyId;


    /**
     * 类型(0 私有;1 共有)
     */
    private Integer type;

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 是否删除（1：未删除；0：已删除）
     */
    private Integer delStatus;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
