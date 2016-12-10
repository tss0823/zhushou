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
	private Long id;

	/**  port * */
	private Integer port;

	/**  clientIp * */
	private String clientIp;

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

	/**  请求方法 * */
	private String reqMethod;

	/**  返回状态 * */
	private Integer httpStatus;

	/**  状态 * */
	private Integer status;

	/**  创建时间 * */
	private Date gmtCreate;

	/**  修改时间 * */
	private Date gmtModify;

	/**  删除状态（0：已删除；1：未删除） * */
	private Boolean delStatus;


	public ProxyContent(){
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getReqData() {
		return reqData;
	}

	public void setReqData(String reqData) {
		this.reqData = reqData;
	}

	public String getResHeader() {
		return resHeader;
	}

	public void setResHeader(String resHeader) {
		this.resHeader = resHeader;
	}

	public String getResData() {
		return resData;
	}

	public void setResData(String resData) {
		this.resData = resData;
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

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModify() {
		return gmtModify;
	}

	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
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
}
