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
 * 代理请求过滤子集
 * @author admin
 *
 * @2017-07-23 15
 */
public class ProxyReqFilterItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ModelFieldComment(value = "ID")
    private Long id;
        
    @ModelFieldComment(value = "创建时间")
    private Date gmtCreate;
        
    @ModelFieldComment(value = "修改时间")
    private Date gmtModify;
        
    @ModelFieldComment(value = "删除状态（0：已删除；1：未删除）")
    private Boolean delState;
        
    @ModelFieldComment(value = "父id")
    private Long parentId;
        
    @ModelFieldComment(value = "状态（0 未启用，1 已启用）")
    private Integer status;
        
    @ModelFieldComment(value = "类型（0 url,1 header,2 body）")
    private Integer type;
        
    @ModelFieldComment(value = "匹配类型（eq,notEq,contains,notContains,regex）")
    private String matchType;
        
    @ModelFieldComment(value = "键")
    private String key;

    @ModelFieldComment(value = "值")
    private String value;

    @ModelFieldComment(value = "过滤类型（0 req,1 重写）")
    private Integer filterType;
        
    
    public ProxyReqFilterItem(){
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
    public void setParentId(Long value) {
        this.parentId = value;
    }
    
    public void setStatus(Integer value) {
        this.status = value;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    public void setType(Integer value) {
        this.type = value;
    }
    
    public Integer getType() {
        return this.type;
    }
    public void setMatchType(String value) {
        this.matchType = value;
    }
    
    public String getMatchType() {
        return this.matchType;
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


    public Long getParentId() {
        return parentId;
    }

    public Integer getFilterType() {
        return filterType;
    }

    public void setFilterType(Integer filterType) {
        this.filterType = filterType;
    }
}