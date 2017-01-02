package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * 模板参数
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
public class AtParameterQuery extends BaseQuery {

    private Long activeId;

    public Long getActiveId() {
        return activeId;
    }

    public void setActiveId(Long activeId) {
        this.activeId = activeId;
    }
}
