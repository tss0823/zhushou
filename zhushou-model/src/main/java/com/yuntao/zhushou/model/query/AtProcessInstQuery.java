package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * Created by shan on 2016/8/1.
 */
public class AtProcessInstQuery extends BaseQuery {

    private Long templateId;

    private Long companyId;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    @Override
    public Long getCompanyId() {
        return companyId;
    }

    @Override
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
