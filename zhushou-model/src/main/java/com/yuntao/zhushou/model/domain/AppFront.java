package com.yuntao.zhushou.model.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shengshan.tang on 2015/12/12 at 15:11
 */
public class AppFront implements Serializable {

    private static final long serialVersionUID = 1L;


    /*/** ID */
    private Long id;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 名称
     */
    private String name;

    /**
     * 负责人Id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModify;

    /**
     * 是否删除
     */
    private Integer delStatus;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 日志
     */
    private String log;

    /**
     * 描述
     */
    private String desc;

    /**
     * git代码名称
     */
    private String codeName;

    /**
     * 域名；user.api.yuntaohongbao.com
     */
    private String domain;

    /**
     * 端口
     */
    private Integer port;


    /**
     *测试 分支
     */
    private String testBranch;

    /**
     *线上 分支
     */
    private String prodBranch;

    private String testAndroidVersion;

    private String testIOSVersion;

    private String prodAndroidVersion;

    private String prodIOSVersion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getTestBranch() {
        return testBranch;
    }

    public void setTestBranch(String testBranch) {
        this.testBranch = testBranch;
    }

    public String getProdBranch() {
        return prodBranch;
    }

    public void setProdBranch(String prodBranch) {
        this.prodBranch = prodBranch;
    }

    public String getTestAndroidVersion() {
        return testAndroidVersion;
    }

    public void setTestAndroidVersion(String testAndroidVersion) {
        this.testAndroidVersion = testAndroidVersion;
    }

    public String getTestIOSVersion() {
        return testIOSVersion;
    }

    public void setTestIOSVersion(String testIOSVersion) {
        this.testIOSVersion = testIOSVersion;
    }

    public String getProdAndroidVersion() {
        return prodAndroidVersion;
    }

    public void setProdAndroidVersion(String prodAndroidVersion) {
        this.prodAndroidVersion = prodAndroidVersion;
    }

    public String getProdIOSVersion() {
        return prodIOSVersion;
    }

    public void setProdIOSVersion(String prodIOSVersion) {
        this.prodIOSVersion = prodIOSVersion;
    }
}
