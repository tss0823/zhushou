/**
 * 
 */
package com.yuntao.zhushou.model.vo.codeBuild;


import com.yuntao.zhushou.model.domain.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangss
 * @2013年8月30日 @下午2:18:56
 */
public class EntityBo extends Entity {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** * 首字母大写英文名 */
    private String            upperEntityEnName;

    /** * 表名 */
    private String            tableName;

    /** * 属性集合 */
    private List<PropertyBo>  propList         = new ArrayList<PropertyBo>();


    public String getUpperEntityEnName() {
        return upperEntityEnName;
    }

    public void setUpperEntityEnName(String upperEntityEnName) {
        this.upperEntityEnName = upperEntityEnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<PropertyBo> getPropList() {
        return propList;
    }

    public void setPropList(List<PropertyBo> propList) {
        this.propList = propList;
    }
}
