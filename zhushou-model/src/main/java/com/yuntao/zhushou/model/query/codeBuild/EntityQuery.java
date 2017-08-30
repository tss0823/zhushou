package com.yuntao.zhushou.model.query.codeBuild;

import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * 代理内容
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
public class EntityQuery extends BaseQuery {

    private String enNameLike;

    private String cnNameLike;

    private Long appId;

    public String getEnNameLike() {
        return enNameLike;
    }

    public void setEnNameLike(String enNameLike) {
        this.enNameLike = enNameLike;
    }

    public String getCnNameLike() {
        return cnNameLike;
    }

    public void setCnNameLike(String cnNameLike) {
        this.cnNameLike = cnNameLike;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
