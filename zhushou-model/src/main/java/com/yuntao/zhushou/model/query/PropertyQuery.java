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
 * 属性
 * @author admin
 *
 * @2018-04-05 08
 */
public class PropertyQuery extends BaseQuery {
    
    private static final long serialVersionUID = 1L;
    
    /**  ID * */
    private Long id;

    /**  创建时间 * */
    private Date gmtCreate;

    /**  修改时间 * */
    private Date gmtModify;

    /**  删除状态（0：已删除；1：未删除） * */
    private Boolean delState = true;

    /**  实体id * */
    private Long entityId;

    /**  英文名称 * */
    private String enName;

    /**  中文名称 * */
    private String cnName;

    /**  数据类型 * */
    private String dataType;

    /**  长度 * */
    private String length;

    /**  是否主键（0：否；1：是） * */
    private Boolean primaryKey;

    /**  默认值 * */
    private String defaultValue;

    /**  是否为空（0：否；1：是） * */
    private Boolean isNull;

    /**  排序 * */
    private Integer orderBy;

    /**  备注 * */
    private String remark;

    

    public PropertyQuery(){}

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