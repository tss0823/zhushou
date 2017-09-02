/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.model.query;

import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * 马克
 * @author admin
 *
 * @2017-08-31 15
 */
public class MarkQuery extends BaseQuery {
    
    private static final long serialVersionUID = 1L;
    
    /**  ID * */
    private Long id;

    /**  索引 * */
    private Integer itemIndex;

    /**  期数 * */
    private Integer periods;

    /**  日期 * */
    private Integer date;

    /**  类型（0=s） * */
    private Integer type;

    /**  值 * */
    private Integer val;

    private Integer startPeriods;

    private Integer endPeriods;

    private Integer topN = 7;

    

    public MarkQuery(){}

    public void setId(Long value) {
        this.id = value;
    }
    
    public Long getId() {
        return this.id;
    }
    public void setItemIndex(Integer value) {
        this.itemIndex = value;
    }
    
    public Integer getItemIndex() {
        return this.itemIndex;
    }
    public void setPeriods(Integer value) {
        this.periods = value;
    }
    
    public Integer getPeriods() {
        return this.periods;
    }
    public void setDate(Integer value) {
        this.date = value;
    }
    
    public Integer getDate() {
        return this.date;
    }
    public void setType(Integer value) {
        this.type = value;
    }
    
    public Integer getType() {
        return this.type;
    }
    public void setVal(Integer value) {
        this.val = value;
    }
    
    public Integer getVal() {
        return this.val;
    }


    public Integer getStartPeriods() {
        return startPeriods;
    }

    public void setStartPeriods(Integer startPeriods) {
        this.startPeriods = startPeriods;
    }

    public Integer getEndPeriods() {
        return endPeriods;
    }

    public void setEndPeriods(Integer endPeriods) {
        this.endPeriods = endPeriods;
    }

    public Integer getTopN() {
        return topN;
    }

    public void setTopN(Integer topN) {
        this.topN = topN;
    }
}