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

    private String cnName;

    private String enName;

    private Long appId;

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
