package com.yuntao.zhushou.model.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shengshan.tang on 2015/12/12 at 15:11
 */
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;


    /*/** ID */
    private Long id;

    /**
     * 名称
     */
    private String name;


    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModify;

    /**
     * 是否删除
     */
    private Integer delStatus;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;


    /**
     * 描述
     */
    private String desc;


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

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

