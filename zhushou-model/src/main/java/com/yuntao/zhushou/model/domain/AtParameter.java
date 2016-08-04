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
 * 模板参数
 * @author admin
 *
 * @2016-07-21 15
 */
public class AtParameter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  ID * */
	private Long id;
		
	/**  活动ID * */
	private Long activeId;
		
	/**  编码 * */
	private String code;
		
	/**  名称 * */
	private String name;
		
	/**  数据类型 * */
	private String dataType;
		
	/**  数据值 * */
	private byte [] dataValue;
		
	/**  规则类型，0:静态，默认；1:数字，2：字符，3：返回值;4：接口中取 * */
	private Integer ruleType;
		
	/**  脚本 * */
	private String script;
		
	/**  创建时间 * */
	private Date gmtCreate;
		
	/**  修改时间 * */
	private Date gmtModify;
		
	/**  是否删除（1：没有；0；已删除） * */
	private Integer delStatus;
		
	
	public AtParameter(){
	}

	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
	public void setActiveId(Long value) {
		this.activeId = value;
	}
	
	public Long getActiveId() {
		return this.activeId;
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
	public void setDataType(String value) {
		this.dataType = value;
	}
	
	public String getDataType() {
		return this.dataType;
	}
	public void setRuleType(Integer value) {
		this.ruleType = value;
	}
	
	public Integer getRuleType() {
		return this.ruleType;
	}
	public void setScript(String value) {
		this.script = value;
	}
	
	public String getScript() {
		return this.script;
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

	public byte[] getDataValue() {
		return dataValue;
	}

	public void setDataValue(byte[] dataValue) {
		this.dataValue = dataValue;
	}
}

