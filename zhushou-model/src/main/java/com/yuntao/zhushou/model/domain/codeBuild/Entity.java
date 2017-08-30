/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.model.domain.codeBuild;


import java.io.Serializable;

/**
 * 实体
 * @author tangss
 *
 * @2014-03-12 09
 */
public class Entity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  ID * */
	private Long id;

	/**  应用ID * */
	private Long appId;

	/**  英文名 * */
	private String cnName;

	/**  中文名 * */
	private String enName;

	/**  项目ID * */
	private Long projectId;

	/**  创建时间 * */
	private java.util.Date gmtCreate;

	/**  修改时间 * */
	private java.util.Date gmtModify;

	/**  删除状态 （0：已删除；1：未删除） * */
	private Boolean delState;

	/**  版本 * */
	private Integer ver;

	/**  简称 * */
	private String shortName;


	public Entity(){
	}

	public void setId(Long value) {
		this.id = value;
	}

	public Long getId() {
		return this.id;
	}
	public void setAppId(Long value) {
		this.appId = value;
	}

	public Long getAppId() {
		return this.appId;
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
	public void setProjectId(Long value) {
		this.projectId = value;
	}

	public Long getProjectId() {
		return this.projectId;
	}
	public void setGmtCreate(java.util.Date value) {
		this.gmtCreate = value;
	}

	public java.util.Date getGmtCreate() {
		return this.gmtCreate;
	}
	public void setGmtModify(java.util.Date value) {
		this.gmtModify = value;
	}

	public java.util.Date getGmtModify() {
		return this.gmtModify;
	}
	public void setDelState(Boolean value) {
		this.delState = value;
	}

	public Boolean getDelState() {
		return this.delState;
	}
	public void setVer(Integer value) {
		this.ver = value;
	}

	public Integer getVer() {
		return this.ver;
	}
	public void setShortName(String value) {
		this.shortName = value;
	}

	public String getShortName() {
		return this.shortName;
	}
	



}

