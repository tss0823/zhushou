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
 * 角色
 * @author admin
 *
 * @2016-08-07 11
 */
public class Role implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  ID * */
	private Long id;
		
	/**  名称 * */
	private String name;
		
	/**  创建时间 * */
	private Date gmtCreate;
		
	/**  修改时间 * */
	private Date gmtModify;
		
	/**  是否删除 * */
	private Integer delStatus;
		
	
	public Role(){
	}

	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return this.name;
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

