package com.yuntao.zhushou.model.param.codeBuild;


import com.yuntao.zhushou.model.domain.Entity;
import com.yuntao.zhushou.model.domain.Property;

import java.util.List;

/**
 * Created by shan on 2017/7/31.
 */
public class EntityParam extends Entity {

    private String clsFullName;

    List<Property> propertyList;

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    public String getClsFullName() {
        return clsFullName;
    }

    public void setClsFullName(String clsFullName) {
        this.clsFullName = clsFullName;
    }
}
