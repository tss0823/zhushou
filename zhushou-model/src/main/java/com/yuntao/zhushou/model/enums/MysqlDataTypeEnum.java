/**
 * 
 */
package com.yuntao.zhushou.model.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 数据类型
 * @author tangss
 *
 * @2013年9月6日 @下午4:22:25
 */
public enum MysqlDataTypeEnum {

	STRING("java.lang.String","varchar"),
	INTEGER("java.lang.Integer","int"),
	LONG("java.lang.Long","bigint"),
	TIME("java.util.Date","datetime"),
	BIGDECIMAL("java.math.BigDecimal","decimal"),
	FLOAT("java.lang.Float","float"),
	BOOLEAN("java.lang.Boolean","tinyint"),
	SHORT("java.lang.Short","tinyint"),
	BYTE("java.lang.Byte","bit");
	
	private String value;
	private String dbValue;
	
	private MysqlDataTypeEnum(String value, String dbValue){
		this.value = value;
		this.dbValue =dbValue;
	}

	public static String getDbValueByJavaValue(String value){
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		for (MysqlDataTypeEnum mysqlDataTypeEnum : MysqlDataTypeEnum.values()) {
			if(mysqlDataTypeEnum.getValue().equals(value)){
				return mysqlDataTypeEnum.getDbValue();
			}
		}
		return null;

	}

	public String getValue() {
		return value;
	}

	public String getDbValue() {
		return dbValue;
	}
	
	
	
}
