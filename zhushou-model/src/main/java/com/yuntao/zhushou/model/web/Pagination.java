/**
 *
 */
package com.yuntao.zhushou.model.web;

import java.io.Serializable;
import java.util.List;

/**
 * @author tangss
 * @2013-6-24 @下午2:06:44
 */
public class Pagination<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8524434997134896430L;

    public static final int DEF_PAGE_SIZE = 10;                  // 默认页大小

    /**
     * 总共记录数量
     */
    private long totalCount;

    /**
     * 页数
     */
    private long pageCount;

    /**
     * 页大小
     */
    private int pageSize;

    /**
     * 当前页数
     */
    private long pageNum;

    /**
     * 起始数量
     */
    private long startRow;

    /**
     * 结束数量
     */
    private long endRow;

    /**
     * 数据结果集
     */
    private List<T> dataList;

    /**
     * @param totalCount
     * @param pageSize
     * @param pageCount
     * @param pageNum
     * @param dataList
     */
    public Pagination(long totalCount, int pageSize, long pageNum) {
        super();
        this.totalCount = totalCount;
        this.pageSize = pageSize == 0 ? DEF_PAGE_SIZE : pageSize;
        this.pageNum = pageNum;
        if (this.pageNum <= 0) {
            this.pageNum = 1;
        }

        // 计算pageCount
        this.pageCount = this.totalCount / this.pageSize;
        if (this.totalCount % this.pageSize > 0) {
            this.pageCount++;
        }
        // 计算startRow 和 endRow
        this.startRow = (this.pageNum - 1) * this.pageSize;
        if (this.startRow < 0) {
            this.startRow = 0;
        }
        this.endRow = startRow + this.pageSize;
        if (this.endRow > totalCount) {
            this.endRow = totalCount;
        }
    }

    /**
     * @param totalCount
     * @param pageSize
     * @param pageNum
     */
    public <T2> Pagination(Pagination<T2> page) {
        this(page.getTotalCount(), page.getPageSize(), page.getPageNum());
    }

    /**
     * @param totalCount
     * @param pageSize
     * @param pageCount
     * @param pageNum
     * @param dataList
     */
    public Pagination(long totalCount, int pageSize, int pageNum, List<T> dataList) {
        this(totalCount, pageSize, pageNum);
        this.dataList = dataList;
    }


    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

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

    public long getStartRow() {
        return startRow;
    }

    public void setStartRow(long startRow) {
        this.startRow = startRow;
    }

    public long getEndRow() {
        return endRow;
    }

    public void setEndRow(long endRow) {
        this.endRow = endRow;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
