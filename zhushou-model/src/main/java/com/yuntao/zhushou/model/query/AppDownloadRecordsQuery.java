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
 * 应用下载记录
 * @author admin
 *
 * @2017-03-18 16
 */
public class AppDownloadRecordsQuery extends BaseQuery {
    
    private static final long serialVersionUID = 1L;
    
    /**  ID * */
    private Long id;
        
    /**  创建时间 * */
    private Date gmtCreate;
        
    /**  修改时间 * */
    private Date gmtModify;
        
    /**  删除状态（0：已删除；1：未删除） * */
    private Boolean delState;
        
    /**  应用版本id * */
    private Long appVersionId;
        
    /**  设备唯一标识 * */
    private String deviceId;
        
    /**  用户id * */
    private Long userId;
        
    /**  IP * */
    private String ip;
        
    


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
    public void setAppVersionId(Long value) {
        this.appVersionId = value;
    }
    
    public Long getAppVersionId() {
        return this.appVersionId;
    }
    public void setDeviceId(String value) {
        this.deviceId = value;
    }
    
    public String getDeviceId() {
        return this.deviceId;
    }
    public void setUserId(Long value) {
        this.userId = value;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    public void setIp(String value) {
        this.ip = value;
    }
    
    public String getIp() {
        return this.ip;
    }
    



}