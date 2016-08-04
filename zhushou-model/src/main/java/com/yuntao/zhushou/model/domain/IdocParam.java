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
 * 接口请求参数
 * @author admin
 *
 * @2016-07-30 20
 */
public class IdocParam implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  ID * */
	private Long id;
		
	/**  编码 * */
	private String code;
		
	/**  名称 * */
	private String name;
		
	/**  主体ID * */
	private Long parentId;
		
	/**  规则 * */
	private String rule;
		
	/**  说明 * */
	private String memo;
		
	/**  状态 * */
	private Integer status;
		
	/**  创建时间 * */
	private Date gmtCreate;
		
	/**  修改时间 * */
	private Date gmtModify;
		
	/**  是否删除（1：没有；0；已删除） * */
	private Integer delStatus;
		
	
	public IdocParam(){
	}

	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
	public void setCode(String value) {
		this.code = value;
	}
	
	public String getCode() {
		return this.code;
	}
	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return this.name;
	}
	public void setParentId(Long value) {
		this.parentId = value;
	}
	
	public Long getParentId() {
		return this.parentId;
	}
	public void setRule(String value) {
		this.rule = value;
	}
	
	public String getRule() {
		return this.rule;
	}
	public void setMemo(String value) {
		this.memo = value;
	}
	
	public String getMemo() {
		return this.memo;
	}
	public void setStatus(Integer value) {
		this.status = value;
	}
	
	public Integer getStatus() {
		return this.status;
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
	public void setDelStatus(Integer value) {
		this.delStatus = value;
	}
	
	public Integer getDelStatus() {
		return this.delStatus;
	}
	



}

