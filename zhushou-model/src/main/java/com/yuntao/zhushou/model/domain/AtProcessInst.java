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
 * 流程实例
 * @author admin
 *
 * @2016-07-21 15
 */
public class AtProcessInst implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  ID * */
	private Long id;
		
	/**  模板ID * */
	private Long templateId;
		
	/**  名称 * */
	private String name;
		
	/**  状态（0 失败，1：成功） * */
	private Integer status;
		
	/**  错误消息 * */
	private String errMsg;
		
	/**  创建时间 * */
	private Date gmtCreate;
		
	/**  修改时间 * */
	private Date gmtModify;

	/**  是否删除（1：没有；0；已删除） * */
	private Integer delStatus;

	/**  运行用户id * */
	private Long userId;


		
	
	public AtProcessInst(){
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
	public void setStatus(Integer value) {
		this.status = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	public void setErrMsg(String value) {
		this.errMsg = value;
	}
	
	public String getErrMsg() {
		return this.errMsg;
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


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
