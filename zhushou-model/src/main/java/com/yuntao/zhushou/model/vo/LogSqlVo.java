package com.yuntao.zhushou.model.vo;

import java.io.Serializable;

/**
 * Created by shan on 2016/5/5.
 */
public class LogSqlVo implements Serializable {


    private String preSql;

    private String parameter;

    private String resultSql;

    private  int result;

    public String getPreSql() {
        return preSql;
    }

    public void setPreSql(String preSql) {
        this.preSql = preSql;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getResultSql() {
        return resultSql;
    }

    public void setResultSql(String resultSql) {
        this.resultSql = resultSql;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
