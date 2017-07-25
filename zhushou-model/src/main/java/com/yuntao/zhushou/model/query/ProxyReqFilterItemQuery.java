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
 * 代理请求过滤子集
 * @author admin
 *
 * @2017-07-23 15
 */
public class ProxyReqFilterItemQuery extends BaseQuery {
    
    private static final long serialVersionUID = 1L;
    
    /**  ID * */
    private Long id;

    /**  创建时间 * */
    private Date gmtCreate;

    /**  修改时间 * */
    private Date gmtModify;

    /**  删除状态（0：已删除；1：未删除） * */
    private Boolean delState = true;

    /**  父id * */
    private Long parentId;

    private Integer filterType;

    /**  连接类型（0 and 1 or） * */
    private Integer joinType;

    /**  状态（0 未启用，1 已启用） * */
    private Integer status;

    /**  类型（0 url,1 header,2 body） * */
    private Integer type;

    /**  匹配类型（eq,notEq,contains,notContains,regex） * */
    private String matchType;

    /**  键 * */
    private String key;

    /**  值 * */
    private String value;

    

    public ProxyReqFilterItemQuery(){}

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
    
    public Long getParentId() {
        return this.parentId;
    }
    public void setJoinType(Integer value) {
        this.joinType = value;
    }
    
    public Integer getJoinType() {
        return this.joinType;
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


    public Integer getFilterType() {
        return filterType;
    }

    public void setFilterType(Integer filterType) {
        this.filterType = filterType;
    }
}