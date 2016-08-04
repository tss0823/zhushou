package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.User;

/**
 * Created by shengshan.tang on 2015/12/26 at 22:13
 */
public class AppVo extends App {

    private String userName;

    private String lastTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
