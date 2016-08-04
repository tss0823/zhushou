package com.yuntao.zhushou.model.query;

/**
 * 接口主体
 * 
 * @author admin
 *
 * @2016-07-30 20
 */
public class IdocUrlQuery extends  BaseQuery {

    /**  URL * */
    private String url;

    /**  名称 * */
    private String name;

    /**  应用 * */
    private String appName;

    /**  模块 * */
    private String module;

    /**  版本 * */
    private String version;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
