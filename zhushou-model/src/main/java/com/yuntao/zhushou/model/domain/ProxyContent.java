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
 * 代理内容
 * @author admin
 *
 * @2016-08-13 21
 */
public class ProxyContent implements Serializable {

	private static final long serialVersionUID = 1L;

	/**  Id * */
	private String id;

	/**
	 * 企业id
	 */
	private Long companyId;

	private Long projectId;

	/**  port * */
	private Integer port;

	/**  clientIp * */
	private String clientIp;

	/**  协议，http https * */
	private String protocol;

	/**  domain * */
	private String domain;

	/**  path URL * */
	private String pathUrl;

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

	/**  返回数据格式 * */
	private String resContentType;

	/**  返回数据长度 * */
	private Integer resLength;

	/**  请求方法 * */
	private String reqMethod;

	/**  返回状态 * */
	private Integer httpStatus;

	/**  状态 * */
	private Integer status;

	/**  请求时间 * */
	private Date gmtRequest;

	/**  返回时间 * */
	private Date gmtResponse;

	/**  删除状态（0：已删除；1：未删除） * */
	private Boolean delStatus;

	private String lastTime;

	/** 是否存储 */
	private boolean store;


	public ProxyContent(){
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(Integer httpStatus) {
		this.httpStatus = httpStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getGmtRequest() {
		return gmtRequest;
	}

	public void setGmtRequest(Date gmtRequest) {
		this.gmtRequest = gmtRequest;
	}

	public Date getGmtResponse() {
		return gmtResponse;
	}

	public void setGmtResponse(Date gmtResponse) {
		this.gmtResponse = gmtResponse;
	}

	public Boolean getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Boolean delStatus) {
		this.delStatus = delStatus;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPathUrl() {
		return pathUrl;
	}

	public void setPathUrl(String pathUrl) {
		this.pathUrl = pathUrl;
	}

	public String getResContentType() {
		return resContentType;
	}

	public void setResContentType(String resContentType) {
		this.resContentType = resContentType;
	}

	public Integer getResLength() {
		return resLength;
	}

	public void setResLength(Integer resLength) {
		this.resLength = resLength;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getReqData() {
		return reqData;
	}

	public void setReqData(String reqData) {
		this.reqData = reqData;
	}

	public String getResData() {
		return resData;
	}

	public void setResData(String resData) {
		this.resData = resData;
	}

	public boolean isStore() {
		return store;
	}

	public void setStore(boolean store) {
		this.store = store;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
}
