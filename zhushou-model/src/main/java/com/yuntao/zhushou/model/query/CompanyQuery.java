package com.yuntao.zhushou.model.query;

/**
 * Created by shan on 2016/3/27.
 */
public class CompanyQuery extends BaseQuery{

    private Long id;

    private String name;

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
}
