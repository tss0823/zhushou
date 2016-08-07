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
 * 权限资源
 * @author admin
 *
 * @2016-08-07 11
 */
public class AuthRes implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  ID * */
	private Long id;
		
	/**  编码 * */
	private String code;
		
	/**  名称 * */
	private String name;
		
	/**  URL * */
	private String url;
		
	/**  模板URL * */
	private String tplUrl;
		
	/**  是否菜单（0：否，1：是） * */
	private Boolean menu;
		
	/**  是否仅展示（0：否，1：是） * */
	private Boolean display;
		
	/**  动作ID * */
	private String actionId;
		
	/**  图标 * */
	private String icon;
		
	/**  级别 * */
	private Integer level;
		
	/**  状态 * */
	private Integer status;
		
	/**  父ID * */
	private Long parentId;
		
	/**  创建时间 * */
	private Date gmtCreate;
		
	/**  修改时间 * */
	private Date gmtModify;
		
	/**  是否删除 * */
	private Integer delStatus;
		
	
	public AuthRes(){
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
	public void setUrl(String value) {
		this.url = value;
	}
	
	public String getUrl() {
		return this.url;
	}
	public void setTplUrl(String value) {
		this.tplUrl = value;
	}
	
	public String getTplUrl() {
		return this.tplUrl;
	}
	public void setMenu(Boolean value) {
		this.menu = value;
	}
	
	public Boolean getMenu() {
		return this.menu;
	}
	public void setDisplay(Boolean value) {
		this.display = value;
	}
	
	public Boolean getDisplay() {
		return this.display;
	}
	public void setActionId(String value) {
		this.actionId = value;
	}
	
	public String getActionId() {
		return this.actionId;
	}
	public void setIcon(String value) {
		this.icon = value;
	}
	
	public String getIcon() {
		return this.icon;
	}
	public void setLevel(Integer value) {
		this.level = value;
	}
	
	public Integer getLevel() {
		return this.level;
	}
	public void setStatus(Integer value) {
		this.status = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	public void setParentId(Long value) {
		this.parentId = value;
	}
	
	public Long getParentId() {
		return this.parentId;
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

