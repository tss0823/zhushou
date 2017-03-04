package com.yuntao.zhushou.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangshengshan on 17-3-4.
 */
public class LogMsgTreeVo implements Serializable{

    private String name;

    private Integer level;

    private List<LogMsgTreeVo> child = new ArrayList<>();

    private List<LogMessageVo> logMessageVoList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<LogMsgTreeVo> getChild() {
        return child;
    }

    public void setChild(List<LogMsgTreeVo> child) {
        this.child = child;
    }

    public List<LogMessageVo> getLogMessageVoList() {
        return logMessageVoList;
    }

    public void setLogMessageVoList(List<LogMessageVo> logMessageVoList) {
        this.logMessageVoList = logMessageVoList;
    }
}
