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
 * 测试模板
 * @author admin
 *
 * @2016-07-21 15
 */
public class AtTemplate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  Id * */
	private Long id;
		
	/**  名称 * */
	private String name;
		
	/**  用户ID * */
	private Long userId;
		
	/**  用户名称 * */
	private String userName;
		
	/**  模式（test,prod） * */
	private String model;
		
	/**  创建时间 * */
	private Date gmtCreate;
		
	/**  修改时间 * */
	private Date gmtModify;
		
	/**  是否删除（1:没有；0：已删除） * */
	private Integer delStatus;
		
	
	public AtTemplate(){
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
	public void setUserId(Long value) {
		this.userId = value;
	}
	
	public Long getUserId() {
		return this.userId;
	}
	public void setUserName(String value) {
		this.userName = value;
	}
	
	public String getUserName() {
		return this.userName;
	}
	public void setModel(String value) {
		this.model = value;
	}
	
	public String getModel() {
		return this.model;
	}
	public void setGmtCreate(Date value) {
		this.gmtCreate = value;
	}
	
	public Date getGmtCreate() {
		return this.gmtCreate;
	}

	public Date getGmtModify() {
		return gmtModify;
	}

	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	public void setDelStatus(Integer value) {
		this.delStatus = value;
	}
	
	public Integer getDelStatus() {
		return this.delStatus;
	}
	



}

