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
 * 属性
 * @author admin
 *
 * @2018-04-05 08
 */
public class Property implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ModelFieldComment(value = "ID")
    private Long id;
        
    @ModelFieldComment(value = "创建时间")
    private Date gmtCreate;
        
    @ModelFieldComment(value = "修改时间")
    private Date gmtModify;
        
    @ModelFieldComment(value = "删除状态（0：已删除；1：未删除）")
    private Boolean delState;
        
    @ModelFieldComment(value = "实体id")
    private Long entityId;
        
    @ModelFieldComment(value = "英文名称")
    private String enName;
        
    @ModelFieldComment(value = "中文名称")
    private String cnName;
        
    @ModelFieldComment(value = "数据类型")
    private String dataType;
        
    @ModelFieldComment(value = "长度")
    private String length;
        
    @ModelFieldComment(value = "是否主键（0：否；1：是）")
    private Boolean primaryKey;
        
    @ModelFieldComment(value = "默认值")
    private String defaultValue;
        
    @ModelFieldComment(value = "是否为空（0：否；1：是）")
    private Boolean isNull;
        
    @ModelFieldComment(value = "排序")
    private Integer orderBy;
        
    @ModelFieldComment(value = "备注")
    private String remark;
        
    
    public Property(){
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(""+id);
        sb.append("#");
        sb.append(cnName);
        sb.append("#");
        sb.append(enName);
        sb.append("#");
        sb.append(dataType);
        sb.append("#");
        sb.append(length);
        return sb.toString();
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
    public void setEntityId(Long value) {
        this.entityId = value;
    }
    
    public Long getEntityId() {
        return this.entityId;
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
    public void setDataType(String value) {
        this.dataType = value;
    }
    
    public String getDataType() {
        return this.dataType;
    }
    public void setLength(String value) {
        this.length = value;
    }
    
    public String getLength() {
        return this.length;
    }
    public void setPrimaryKey(Boolean value) {
        this.primaryKey = value;
    }
    
    public Boolean getPrimaryKey() {
        return this.primaryKey;
    }
    public void setDefaultValue(String value) {
        this.defaultValue = value;
    }
    
    public String getDefaultValue() {
        return this.defaultValue;
    }
    public void setIsNull(Boolean value) {
        this.isNull = value;
    }
    
    public Boolean getIsNull() {
        return this.isNull;
    }
    public void setOrderBy(Integer value) {
        this.orderBy = value;
    }
    
    public Integer getOrderBy() {
        return this.orderBy;
    }
    public void setRemark(String value) {
        this.remark = value;
    }
    
    public String getRemark() {
        return this.remark;
    }





}