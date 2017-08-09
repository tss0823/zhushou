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
 * 测试变量
 * @author admin
 *
 * @2017-07-31 16
 */
public class AtVariableQuery extends BaseQuery {
    
    private static final long serialVersionUID = 1L;
    
    /**  ID * */
    private Long id;

    /**  创建时间 * */
    private Date gmtCreate;

    /**  修改时间 * */
    private Date gmtModify;

    /**  删除状态（0：已删除；1：未删除） * */
    private Boolean delState = true;

    /**  key * */
    private String key;

    /**  值 * */
    private String value;

    /**  状态（0 未启用，1 启用） * */
    private Integer status;

    /**  范围（0 局域，1 全局） * */
    private Integer scope;

    /**  模板id * */
    private Long templateId;

    /**  用户id * */
    private Long userId;

    

    public AtVariableQuery(){}

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
    public void setKey(String value) {
        this.key = value;
    }
    
    public String getKey() {
        return this.key;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return this.value;
    }
    public void setStatus(Integer value) {
        this.status = value;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    public void setScope(Integer value) {
        this.scope = value;
    }
    
    public Integer getScope() {
        return this.scope;
    }
    public void setTemplateId(Long value) {
        this.templateId = value;
    }
    
    public Long getTemplateId() {
        return this.templateId;
    }
    public void setUserId(Long value) {
        this.userId = value;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    



}