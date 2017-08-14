/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.model.domain;

import com.yuntao.zhushou.common.annation.ModelFieldComment;

import java.io.Serializable;
import java.util.Date;

/**
 * 测试变量
 * @author admin
 *
 * @2017-07-31 16
 */
public class AtVariable implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ModelFieldComment(value = "ID")
    private Long id;
        
    @ModelFieldComment(value = "创建时间")
    private Date gmtCreate;
        
    @ModelFieldComment(value = "修改时间")
    private Date gmtModify;
        
    @ModelFieldComment(value = "删除状态（0：已删除；1：未删除）")
    private Boolean delState;
        
    @ModelFieldComment(value = "key")
    private String key;
        
    @ModelFieldComment(value = "值")
    private String value;
        
    @ModelFieldComment(value = "状态（0 未启用，1 启用）")
    private Integer status;
        
    @ModelFieldComment(value = "范围（0 局域，1 全局）")
    private Integer scope;
        
    @ModelFieldComment(value = "模板id")
    private Long templateId;

    @ModelFieldComment(value = "用户id")
    private Long userId;

    @ModelFieldComment(value = "企业id")
    private Long companyId;
        
    
    public AtVariable(){
    }

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


    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}