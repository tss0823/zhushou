/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.common.web.BaseQuery;

import java.util.Date;

/**
 * 应用版本
 *
 * @author admin
 * @2017-03-18 16
 */
public class AppVersionQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * ID *
     */
    private Long id;

    /**
     * 创建时间 *
     */
    private Date gmtCreate;

    /**
     * 修改时间 *
     */
    private Date gmtModify;

    /**
     * 删除状态（0：已删除；1：未删除） *
     */
    private Boolean delState;

    /**
     * 公司id *
     */
    private Long companyId;

    /**
     * 应用名称 *
     */
    private String appName;

    /**
     * 模式（test or prod） *
     */
    private String model;

    /**
     * 类型（android or ios） *
     */
    private String type;

    /**
     * 版本号（x.y.z） *
     */
    private String version;

    /**
     * app下载链接地址 *
     */
    private String appUrl;

    /**
     * 是否强制更新 *
     */
    private Boolean forceUpdate;

    private Integer status;


    public void setId(Long value) {
        this.id = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setGmtCreate(Date value) {
        this.gmtCreate = value;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtModify(Date value) {
        this.gmtModify = value;
    }

    public Date getGmtModify() {
        return this.gmtModify;
    }

    public void setDelState(Boolean value) {
        this.delState = value;
    }

    public Boolean getDelState() {
        return this.delState;
    }

    public void setCompanyId(Long value) {
        this.companyId = value;
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public void setAppName(String value) {
        this.appName = value;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setModel(String value) {
        this.model = value;
    }

    public String getModel() {
        return this.model;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getType() {
        return this.type;
    }

    public void setVersion(String value) {
        this.version = value;
    }

    public String getVersion() {
        return this.version;
    }

    public void setAppUrl(String value) {
        this.appUrl = value;
    }

    public String getAppUrl() {
        return this.appUrl;
    }

    public void setForceUpdate(Boolean value) {
        this.forceUpdate = value;
    }

    public Boolean getForceUpdate() {
        return this.forceUpdate;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}