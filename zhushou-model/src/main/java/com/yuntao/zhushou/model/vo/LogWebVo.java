package com.yuntao.zhushou.model.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shan on 2016/5/5.
 */
public class LogWebVo extends  LogVo {


    private String errMsg;

    private String resFormatMsg;

    private List<LogMessageVo> logSqlVoList = new ArrayList<>();

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getResFormatMsg() {
        return resFormatMsg;
    }

    public void setResFormatMsg(String resFormatMsg) {
        this.resFormatMsg = resFormatMsg;
    }

    public List<LogMessageVo> getLogSqlVoList() {
        return logSqlVoList;
    }

    public void setLogSqlVoList(List<LogMessageVo> logSqlVoList) {
        this.logSqlVoList = logSqlVoList;
    }
}
