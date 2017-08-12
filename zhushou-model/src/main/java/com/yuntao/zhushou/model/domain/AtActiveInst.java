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
 * 活动实例
 * @author admin
 *
 * @2016-07-21 15
 */
public class AtActiveInst implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  ID * */
	private Long id;

	/**  流程实例ID * */
	private Long processInstId;

	/**  活动iD * */
	private Long activeId;
		
	/**  名称 * */
	private String name;
		
	/**  状态（0 失败，1：成功） * */
	private Integer status;
		
	/**  http返回状态 * */
	private Integer httpStatus;
		
	/**  错误消息 * */
	private String errMsg;
		
	/**  返回结果 * */
	private String result;
		
	/**  创建时间 * */
	private Date gmtCreate;
		
	/**  修改时间 * */
	private Date gmtModify;
		
	/**  是否删除（1：没有；0；已删除） * */
	private Integer delStatus;

	private String param;

	private String reqHeader;

	private String resHeader;
		
	
	public AtActiveInst(){
	}

	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
	public void setProcessInstId(Long value) {
		this.processInstId = value;
	}
	
	public Long getProcessInstId() {
		return this.processInstId;
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
	public void setHttpStatus(Integer value) {
		this.httpStatus = value;
	}
	
	public Integer getHttpStatus() {
		return this.httpStatus;
	}
	public void setErrMsg(String value) {
		this.errMsg = value;
	}
	
	public String getErrMsg() {
		return this.errMsg;
	}
	public void setResult(String value) {
		this.result = value;
	}
	
	public String getResult() {
		return this.result;
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


	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getReqHeader() {
		return reqHeader;
	}

	public void setReqHeader(String reqHeader) {
		this.reqHeader = reqHeader;
	}

	public String getResHeader() {
		return resHeader;
	}

	public void setResHeader(String resHeader) {
		this.resHeader = resHeader;
	}

	public Long getActiveId() {
		return activeId;
	}

	public void setActiveId(Long activeId) {
		this.activeId = activeId;
	}
}
