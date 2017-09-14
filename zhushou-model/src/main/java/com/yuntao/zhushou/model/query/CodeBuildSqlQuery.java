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
 * 代码生成sql
 * @author admin
 *
 * @2017-09-14 17
 */
public class CodeBuildSqlQuery extends BaseQuery {
    
    private static final long serialVersionUID = 1L;
    
    /**  ID * */
    private Long id;

    /**  创建时间 * */
    private Date gmtCreate;

    /**  修改时间 * */
    private Date gmtModify;

    /**  删除状态（0：已删除；1：未删除） * */
    private Boolean delState = true;

    /**  企业id * */
    private Long companyId;

    /**  内容 * */
    private String content;

    /**  用户id * */
    private Long userId;

    

    public CodeBuildSqlQuery(){}

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
    public void setContent(String value) {
        this.content = value;
    }
    
    public String getContent() {
        return this.content;
    }
    public void setUserId(Long value) {
        this.userId = value;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    



}