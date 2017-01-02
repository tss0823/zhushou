package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * 接口请求参数
 * 
 * @author admin
 *
 * @2016-07-30 20
 */
public class IdocParamQuery extends BaseQuery {

    private Long parentId;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
