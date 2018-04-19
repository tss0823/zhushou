package com.yuntao.zhushou.model.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shengshan.tang on 2015/12/12 at 15:11
 */
public class Host implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 名称
     */
    private String name;

    /**
     * 内网
     */
    private String eth0;

    /**
     * 外网
     */
    private String eth1;

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

    private String username;

    private String pwd;

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

    public String getEth0() {
        return eth0;
    }

    public void setEth0(String eth0) {
        this.eth0 = eth0;
    }

    public String getEth1() {
        return eth1;
    }

    public void setEth1(String eth1) {
        this.eth1 = eth1;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
