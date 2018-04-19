/**
 *
 */
package com.yuntao.zhushou.model.vo.codeBuild;


import com.yuntao.zhushou.model.domain.Property;

/**
 * @author tangss
 * @2013年9月2日 @上午9:40:15
 */
public class PropertyBo extends Property {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

//    private String columnType;

    private String upperEnName;

    private String columnName;


//    public String getColumnType() {
//        return columnType;
//    }
//
//    public void setColumnType(String columnType) {
//        this.columnType = columnType;
//    }

    public String getUpperEnName() {
        return upperEnName;
    }

    public void setUpperEnName(String upperEnName) {
        this.upperEnName = upperEnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }



}
