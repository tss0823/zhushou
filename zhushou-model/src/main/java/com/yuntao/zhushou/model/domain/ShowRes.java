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
 * 展示资源
 * @author admin
 *
 * @2016-08-13 21
 */
public class ShowRes implements Serializable {

	private static final long serialVersionUID = 1L;

	/**  Id * */
	private Long id;

	/**  name * */
	private String name;

	/**  类型(0 设计稿,1 效果图) * */
	private Integer type;

	/**  应用名称 * */
	private String appName;

	/**  模块 * */
	private String module;

	/**  图片地址 * */
	private String imgUrl;

	/**  h5地址 * */
	private String h5Url;

	/**  版本 * */
	private String version;

	/**  是否首页  */
	private Boolean index;

	/**  状态 * */
	private Integer status;

	/**  创建时间 * */
	private Date gmtCreate;

	/**  修改时间 * */
	private Date gmtModify;

	/**  删除状态（0：已删除；1：未删除） * */
	private Boolean delStatus;


	public ShowRes(){
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getH5Url() {
		return h5Url;
	}

	public void setH5Url(String h5Url) {
		this.h5Url = h5Url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Boolean getIndex() {
		return index;
	}

	public void setIndex(Boolean index) {
		this.index = index;
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
}

