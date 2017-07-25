/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.model.domain;

import com.yuntao.zhushou.common.annation.ModelFieldComment;

import java.io.Serializable;
import java.util.Date;

/**
 * 代理返回重写
 * @author admin
 *
 * @2017-07-23 15
 */
public class ProxyResRewrite implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ModelFieldComment(value = "ID")
    private Long id;
        
    @ModelFieldComment(value = "创建时间")
    private Date gmtCreate;
        
    @ModelFieldComment(value = "修改时间")
    private Date gmtModify;
        
    @ModelFieldComment(value = "删除状态（0：已删除；1：未删除）")
    private Boolean delState;
        
    @ModelFieldComment(value = "名称")
    private String name;
        
    @ModelFieldComment(value = "端口")
    private Integer port;
        
    @ModelFieldComment(value = "状态（0 未启用，1 已启用）")
    private Integer status;
        
    @ModelFieldComment(value = "用户id")
    private Long userId;

    @ModelFieldComment(value = "连接类型（0 and 1 or）")
    private Integer joinType;

    @ModelFieldComment(value = "返回类型 （0 header,1 body）")
    private Integer resType;
        
    @ModelFieldComment(value = "返回模型（ 0 静态，1 动态)")
    private Integer resModel;
        
    @ModelFieldComment(value = "返回内容/跳转内容")
    private String data;
        
    
    public ProxyResRewrite(){
    }

    public void setId(Long value) {
        this.id = value;
    }
    
    public Long getId() {
        return this.id;
    }
    public void setGmtCreate(Date value) {
        this.gmtCreate = value;
    }
    
    public Date getGmtCreate() {
        return this.gmtCreate;
    }
    public void setGmtModify(Date value) {
        this.gmtModify = value;
    }
    
    public Date getGmtModify() {
        return this.gmtModify;
    }
    public void setDelState(Boolean value) {
        this.delState = value;
    }
    
    public Boolean getDelState() {
        return this.delState;
    }
    public void setName(String value) {
        this.name = value;
    }
    
    public String getName() {
        return this.name;
    }
    public void setPort(Integer value) {
        this.port = value;
    }
    
    public Integer getPort() {
        return this.port;
    }
    public void setStatus(Integer value) {
        this.status = value;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    public void setUserId(Long value) {
        this.userId = value;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    public void setResType(Integer value) {
        this.resType = value;
    }
    
    public Integer getResType() {
        return this.resType;
    }
    public void setResModel(Integer value) {
        this.resModel = value;
    }
    
    public Integer getResModel() {
        return this.resModel;
    }
    public void setData(String value) {
        this.data = value;
    }
    
    public String getData() {
        return this.data;
    }


    public Integer getJoinType() {
        return joinType;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }
}