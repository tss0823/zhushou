package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.model.web.Pagination;

import java.io.Serializable;

/**
 * Created by shengshan.tang on 2015/11/27 at 14:12
 */
public class BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageSize = 10; // default 10

    private long pageNum = 1; // 第几页

    private Pagination pagination;

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
}
