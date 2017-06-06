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
 * 告警事件
 * @author admin
 *
 * @2017-06-05 00
 */
public class WarnEventQuery extends BaseQuery {
    
    private static final long serialVersionUID = 1L;
    
    /**  ID * */
    private Long id;
        
    /**  创建时间 * */
    private Date gmtCreate;
        
    /**  修改时间 * */
    private Date gmtModify;
        
    /**  删除状态（0：已删除；1：未删除） * */
    private Boolean delState;
        
    /**  公司id * */
    private Long companyId;
        
    /**  名称 * */
    private String name;
        
    /**  内容 * */
    private String content;
        
    /**  计划执行时间 * */
    private Date execTime;
        
    /**  发送短信（0 否，1 是） * */
    private Integer sms;
        
    /**  发送邮件（0 否，1 是） * */
    private Integer email;
        
    /**  发送微信（0 否，1 是） * */
    private Integer weixin;
        
    /**  操作状态 (0 未操作，1 已执行) * */
    private Integer status;
        
    /**  发送的用户ids(多个用逗号隔开) * */
    private String userIds;

    private String execTimeEnd;
        
    


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
    public void setName(String value) {
        this.name = value;
    }
    
    public String getName() {
        return this.name;
    }
    public void setContent(String value) {
        this.content = value;
    }
    
    public String getContent() {
        return this.content;
    }
    public void setExecTime(Date value) {
        this.execTime = value;
    }
    
    public Date getExecTime() {
        return this.execTime;
    }
    public void setSms(Integer value) {
        this.sms = value;
    }
    
    public Integer getSms() {
        return this.sms;
    }
    public void setEmail(Integer value) {
        this.email = value;
    }
    
    public Integer getEmail() {
        return this.email;
    }
    public void setWeixin(Integer value) {
        this.weixin = value;
    }
    
    public Integer getWeixin() {
        return this.weixin;
    }
    public void setStatus(Integer value) {
        this.status = value;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    public void setUserIds(String value) {
        this.userIds = value;
    }
    
    public String getUserIds() {
        return this.userIds;
    }


    public String getExecTimeEnd() {
        return execTimeEnd;
    }

    public void setExecTimeEnd(String execTimeEnd) {
        this.execTimeEnd = execTimeEnd;
    }
}