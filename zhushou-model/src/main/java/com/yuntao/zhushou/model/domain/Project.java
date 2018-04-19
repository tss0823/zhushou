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
 * 项目
 * @author admin
 *
 * @2018-04-05 08
 */
public class Project implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ModelFieldComment(value = "ID")
    private Long id;
        
    @ModelFieldComment(value = "创建时间")
    private Date gmtCreate;
        
    @ModelFieldComment(value = "修改时间")
    private Date gmtModify;
        
    @ModelFieldComment(value = "删除状态（0：已删除；1：未删除）")
    private Boolean delState;

    @ModelFieldComment(value = "企业id")
    private Long companyId;

    @ModelFieldComment(value = "模版id")
    private Long templateId;

    @ModelFieldComment(value = "名称")
    private String name;

    @ModelFieldComment(value = "英文名称")
    private String enName;

    @ModelFieldComment(value = "git代码名称")
    private String codeName;
        
    @ModelFieldComment(value = "描述")
    private String desc;

    @ModelFieldComment(value = "包名")
    private java.lang.String packageName;


    
    public Project(){
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
    public void setDesc(String value) {
        this.desc = value;
    }
    
    public String getDesc() {
        return this.desc;
    }


    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
}