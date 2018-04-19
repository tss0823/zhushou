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
 * 附件
 * @author admin
 *
 * @2018-04-08 18
 */
public class AttachmentQuery extends BaseQuery {
    
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

    /**  内容 * */
    private byte [] content;

    /**  下载链接 * */
    private String downloadUrl;

    /**  公司id * */
    private Long companyId;

    

    public AttachmentQuery(){}

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
    public void setContent(byte [] value) {
        this.content = value;
    }
    
    public byte [] getContent() {
        return this.content;
    }
    public void setDownloadUrl(String value) {
        this.downloadUrl = value;
    }
    
    public String getDownloadUrl() {
        return this.downloadUrl;
    }
    public void setCompanyId(Long value) {
        this.companyId = value;
    }
    
    public Long getCompanyId() {
        return this.companyId;
    }
    



}