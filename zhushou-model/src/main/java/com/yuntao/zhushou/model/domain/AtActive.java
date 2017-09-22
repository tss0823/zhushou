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
 * 活动模板
 * @author admin
 *
 * @2016-07-21 15
 */
public class AtActive implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  ID * */
	private Long id;

	/**  模板ID * */
	private Long templateId;

	/**  日志栈id * */
	private String logStackId;
		
	/**  名称 * */
	private String name;
		
	/**  URL * */
	private String url;
		
	/**  请求方法 * */
	private String method;
		
	/**  类型 * */
	private String reqContentType;
		
	/**  请求头 * */
	private String headerRow;
		
	/**  创建时间 * */
	private Date gmtCreate;
		
	/**  修改时间 * */
	private Date gmtModify;

	/**  是否删除（1：没有；0；已删除） * */
	private Integer delStatus;

	/**  排序 * */
	private Integer orderIndex;
		
	
	public AtActive(){
	}

	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
	public void setTemplateId(Long value) {
		this.templateId = value;
	}
	
	public Long getTemplateId() {
		return this.templateId;
	}
	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return this.name;
	}
	public void setUrl(String value) {
		this.url = value;
	}
	
	public String getUrl() {
		return this.url;
	}
	public void setMethod(String value) {
		this.method = value;
	}
	
	public String getMethod() {
		return this.method;
	}
	public void setReqContentType(String value) {
		this.reqContentType = value;
	}
	
	public String getReqContentType() {
		return this.reqContentType;
	}
	public void setHeaderRow(String value) {
		this.headerRow = value;
	}

	public String getHeaderRow() {
		return this.headerRow;
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


	public String getLogStackId() {
		return logStackId;
	}

	public void setLogStackId(String logStackId) {
		this.logStackId = logStackId;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
}
