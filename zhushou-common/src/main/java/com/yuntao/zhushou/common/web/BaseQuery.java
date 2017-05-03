package com.yuntao.zhushou.common.web;




import java.io.Serializable;

/**
 * Created by shengshan.tang on 2015/11/27 at 14:12
 */
public class BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageSize = 10; // default 10

    private long pageNum = 1; // 第几页

    private Pagination pagination;

    private Long userId;

    private Long companyId;

    private String key;

    private String codeName;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}
