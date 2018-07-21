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
 * 实体
 * @author admin
 *
 * @2018-04-05 08
 */
public class Entity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ModelFieldComment(value = "ID")
    private Long id;
        
    @ModelFieldComment(value = "创建时间")
    private Date gmtCreate;
        
    @ModelFieldComment(value = "修改时间")
    private Date gmtModify;
        
    @ModelFieldComment(value = "删除状态（0：已删除；1：未删除）")
    private Boolean delState;

    @ModelFieldComment(value = "项目id")
    private Long projectId;

    @ModelFieldComment(value = "应用id")
    private Long appId;
        
    @ModelFieldComment(value = "英文名称")
    private String enName;

    @ModelFieldComment(value = "中文名称")
    private String cnName;

    @ModelFieldComment(value = "表名称")
    private String tableName;
        
    
    public Entity(){
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
    public void setAppId(Long value) {
        this.appId = value;
    }
    
    public Long getAppId() {
        return this.appId;
    }
    public void setEnName(String value) {
        this.enName = value;
    }
    
    public String getEnName() {
        return this.enName;
    }
    public void setCnName(String value) {
        this.cnName = value;
    }
    
    public String getCnName() {
        return this.cnName;
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}