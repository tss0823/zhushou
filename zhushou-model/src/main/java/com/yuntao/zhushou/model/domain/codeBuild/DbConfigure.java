/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.model.domain.codeBuild;


import java.io.Serializable;

/**
 * 数据库配置
 * @author tangss
 *
 * @2014-03-11 14
 */
public class DbConfigure implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  ID * */
	private Long id;

	/**  地址 * */
	private String url;

	/**  驱动 * */
	private String driver;

	/**  用户名 * */
	private String user;

	/**  密码 * */
	private String password;

	/**  类型 * */
	private String type;

	/**  删除状态（0：已删除；1：未删除） * */
	private Boolean delState;

	/**  表空间 * */
	private String tableSpace;

	/**  名称 * */
	private String name;


	public DbConfigure(){
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
	public void setDriver(String value) {
		this.driver = value;
	}

	public String getDriver() {
		return this.driver;
	}
	public void setUser(String value) {
		this.user = value;
	}

	public String getUser() {
		return this.user;
	}
	public void setPassword(String value) {
		this.password = value;
	}

	public String getPassword() {
		return this.password;
	}
	public void setType(String value) {
		this.type = value;
	}

	public String getType() {
		return this.type;
	}
	public void setDelState(Boolean value) {
		this.delState = value;
	}

	public Boolean getDelState() {
		return this.delState;
	}
	public void setTableSpace(String value) {
		this.tableSpace = value;
	}

	public String getTableSpace() {
		return this.tableSpace;
	}
	public void setName(String value) {
		this.name = value;
	}

	public String getName() {
		return this.name;
	}
	



}

