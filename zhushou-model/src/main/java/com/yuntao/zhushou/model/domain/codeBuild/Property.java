/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.model.domain.codeBuild;


import java.io.Serializable;

/**
 * 属性
 * 
 * @author tangss
 * @2014-02-08 13
 */
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID * */
    private Long    id;

    /** 中文名 * */
    private String  cnName;

    /** 英文名 * */
    private String  enName;

    /** 实体id * */
    private Long    entityId;

    /** 创建时间 * */
    private Long    gmtCreate;

    /** 修改时间 * */
    private Long    gmtModify;

    /** 删除状态 （0：已删除；1：未删除） * */
    private Boolean delState;

    /** 数据类型 * */
    private String  dataType;

    /** 长度 * */
    private String  length;

    /** 是否主键（0：否；1：是） * */
    private Boolean primaryKey;

    /** 默认值 * */
    private String  defaultValue;

    /** 是否为空（0：否；1：是） * */
    private Boolean isNull;

    /** 排序 * */
    private Integer order;

    /** 输入类型(0：手动输入；1：枚举；2：数据集） * */
    private Integer sourceType;

    /** 输入来源 * */
    private String  source;

    /** 备注 * */
    private String  remark;

    /** 校验分组 * */
    private String  checkGroup;

    /** 版本 * */
    private Integer ver;

    public Property(){
    }

    public void setId(Long value) {
        this.id = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setCnName(String value) {
        this.cnName = value;
    }

    public String getCnName() {
        return this.cnName;
    }

    public void setEnName(String value) {
        this.enName = value;
    }

    public String getEnName() {
        return this.enName;
    }

    public void setEntityId(Long value) {
        this.entityId = value;
    }

    public Long getEntityId() {
        return this.entityId;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Long gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Boolean getNull() {
        return isNull;
    }

    public void setNull(Boolean aNull) {
        isNull = aNull;
    }

    public void setDelState(Boolean value) {
        this.delState = value;
    }

    public Boolean getDelState() {
        return this.delState;
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

    public void setOrder(Integer value) {
        this.order = value;
    }

    public Integer getOrder() {
        return this.order;
    }

    public void setSourceType(Integer value) {
        this.sourceType = value;
    }

    public Integer getSourceType() {
        return this.sourceType;
    }

    public void setSource(String value) {
        this.source = value;
    }

    public String getSource() {
        return this.source;
    }

    public void setRemark(String value) {
        this.remark = value;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setCheckGroup(String value) {
        this.checkGroup = value;
    }

    public String getCheckGroup() {
        return this.checkGroup;
    }

    public void setVer(Integer value) {
        this.ver = value;
    }

    public Integer getVer() {
        return this.ver;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(entityId);
        sb.append("#");
        sb.append(cnName);
        sb.append("#");
        sb.append(enName);
        sb.append("#");
        sb.append(dataType);
        sb.append("#");
        sb.append(length);
        sb.append("#");
        sb.append(primaryKey);
        sb.append("#");
        sb.append(defaultValue);
        sb.append("#");
        sb.append(isNull);
        sb.append("#");
        sb.append(sourceType);
        sb.append("#");
        sb.append(source);
        sb.append("#");
        sb.append(remark);
        sb.append("#");
        sb.append(checkGroup);
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }

}
