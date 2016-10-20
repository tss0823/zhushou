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
 * 请求内容
 * @author admin
 *
 * @2016-08-13 21
 */
public class ReqContent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  Id * */
	private Long id;
		
	/**  URL * */
	private String url;
		
	/**  请求头 * */
	private String reqHeader;
		
	/**  请求数据 * */
	private String reqData;
		
	/**  返回头 * */
	private String resHeader;
		
	/**  返回数据 * */
	private String resData;
		
	/**  请求方法 * */
	private String reqMethod;
		
	/**  返回状态 * */
	private Integer httpStatus;
		
	/**  操作人用户ID * */
	private Long userId;
		
	/**  操作人用户姓名 * */
	private String userName;
		
	/**  模式（test,prod） * */
	private String model;
		
	/**  应用 * */
	private String appName;
		
	/**  名称 * */
	private String name;
		
	/**  状态 * */
	private Integer status;
		
	/**  创建时间 * */
	private Date gmtCreate;
		
	/**  修改时间 * */
	private Date gmtModify;
		
	/**  删除状态（0：已删除；1：未删除） * */
	private Boolean delStatus;
		
	
	public ReqContent(){
	}

	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
	public void setUrl(String value) {
		this.url = value;
	}
	
	public String getUrl() {
		return this.url;
	}
	public void setReqHeader(String value) {
		this.reqHeader = value;
	}
	
	public String getReqHeader() {
		return this.reqHeader;
	}
	public void setReqData(String value) {
		this.reqData = value;
	}
	
	public String getReqData() {
		return this.reqData;
	}
	public void setResHeader(String value) {
		this.resHeader = value;
	}
	
	public String getResHeader() {
		return this.resHeader;
	}
	public void setResData(String value) {
		this.resData = value;
	}
	
	public String getResData() {
		return this.resData;
	}
	public void setReqMethod(String value) {
		this.reqMethod = value;
	}
	
	public String getReqMethod() {
		return this.reqMethod;
	}
	public void setHttpStatus(Integer value) {
		this.httpStatus = value;
	}
	
	public Integer getHttpStatus() {
		return this.httpStatus;
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
	public void setAppName(String value) {
		this.appName = value;
	}
	
	public String getAppName() {
		return this.appName;
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

	public Boolean getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Boolean delStatus) {
		this.delStatus = delStatus;
	}
}

