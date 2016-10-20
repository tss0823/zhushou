package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.DeployLog;

/**
 * Created by shengshan.tang on 2015/12/26 at 22:13
 */
public class DeployLogVo extends DeployLog {


    private String lastTime;

    private String time;

    private String logText;

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
