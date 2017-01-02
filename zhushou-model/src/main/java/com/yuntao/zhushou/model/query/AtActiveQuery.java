package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * 活动模板
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
public class AtActiveQuery extends BaseQuery {

    private Long templateId;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
}
