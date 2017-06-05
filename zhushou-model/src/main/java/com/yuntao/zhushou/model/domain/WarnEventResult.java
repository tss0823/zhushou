/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.model.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警事件发送结果
 * @author admin
 *
 * @2017-06-05 00
 */
public class WarnEventResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**  ID * */
    private Long id;
        
    /**  创建时间 * */
    private Date gmtCreate;
        
    /**  修改时间 * */
    private Date gmtModify;
        
    /**  删除状态（0：已删除；1：未删除） * */
    private Boolean delState;
        
    /**  告警事件id * */
    private Long warnEventId;
        
    /**  类型（0 sms,1 email,2 weixin） * */
    private Integer type;
        
    /**  状态（0 失败，1 成功） * */
    private Integer status;
        
    /**  用户id * */
    private Long userId;
        
    /**  尝试重试发送次数 * */
    private Integer tryCount;
        
    /**  发送内容 * */
    private String content;
        
    
    public WarnEventResult(){
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
    public void setWarnEventId(Long value) {
        this.warnEventId = value;
    }
    
    public Long getWarnEventId() {
        return this.warnEventId;
    }
    public void setType(Integer value) {
        this.type = value;
    }
    
    public Integer getType() {
        return this.type;
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
    public void setTryCount(Integer value) {
        this.tryCount = value;
    }
    
    public Integer getTryCount() {
        return this.tryCount;
    }
    public void setContent(String value) {
        this.content = value;
    }
    
    public String getContent() {
        return this.content;
    }
    



}