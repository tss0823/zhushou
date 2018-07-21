package com.yuntao.zhushou.zplugin.sqlmap.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: shengshan.tang
 * @date: 2018/7/15 下午10:06
 */
public class SqlMapMethod {

    private String name;

    private List<SqlMapParam> sqlMapParamList = new ArrayList<>();

    private String returnType;

    /** 生成范围 */
    private String genScope;

    private Set importSet = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSqlMapParam(SqlMapParam sqlMapParam) {
         this.sqlMapParamList.add(sqlMapParam);
    }

    public List<SqlMapParam> getSqlMapParamList() {
        return sqlMapParamList;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getGenScope() {
        return genScope;
    }

    public void setGenScope(String genScope) {
        this.genScope = genScope;
    }

    public Set getImportSet() {
        return importSet;
    }

    public void addImportCls(String importCls) {
        this.importSet.add(importCls);
    }
}
