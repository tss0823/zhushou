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
 * 代理请求过滤
 * @author admin
 *
 * @2017-07-23 15
 */
public class ProxyReqFilterQuery extends BaseQuery {
    
    private static final long serialVersionUID = 1L;
    
    /**  ID * */
    private Long id;

    /**  创建时间 * */
    private Date gmtCreate;

    /**  修改时间 * */
    private Date gmtModify;

    /**  删除状态（0：已删除；1：未删除） * */
    private Boolean delState = true;

    /**  名称 * */
    private String name;

    /**  端口 * */
    private Integer port;

    /**  状态（0 未启用，1 已启用） * */
    private Integer status;

    /**  用户id * */
    private Long userId;

    

    public ProxyReqFilterQuery(){}

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
    public void setName(String value) {
        this.name = value;
    }
    
    public String getName() {
        return this.name;
    }
    public void setPort(Integer value) {
        this.port = value;
    }
    
    public Integer getPort() {
        return this.port;
    }
    public void setStatus(Integer value) {
        this.status = value;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    public void setUserId(Long value) {
        this.userId = value;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    



}