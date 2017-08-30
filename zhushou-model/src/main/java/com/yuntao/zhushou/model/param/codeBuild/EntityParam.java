package com.yuntao.zhushou.model.param.codeBuild;

import com.yuntao.zhushou.model.domain.codeBuild.Entity;
import com.yuntao.zhushou.model.domain.codeBuild.Property;

import java.util.List;

/**
 * Created by shan on 2017/7/31.
 */
public class EntityParam extends Entity {

    List<Property> propertyList;

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }
}
