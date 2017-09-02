/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.model.domain;


import com.yuntao.zhushou.common.annation.ModelFieldComment;

import java.io.Serializable;


/**
 * 马克
 * @author admin
 *
 * @2017-08-31 15
 */
public class Mark implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ModelFieldComment(value = "ID")
    private Long id;
        
    @ModelFieldComment(value = "索引")
    private Integer itemIndex;
        
    @ModelFieldComment(value = "期数")
    private Integer periods;
        
    @ModelFieldComment(value = "日期")
    private Integer date;
        
    @ModelFieldComment(value = "类型（0=s）")
    private Integer type;
        
    @ModelFieldComment(value = "值")
    private Integer val;
        
    
    public Mark(){
    }

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
    



}