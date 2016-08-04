package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.User;

/**
 * Created by shengshan.tang on 2015/12/26 at 22:13
 */
public class UserVo extends User {

    private String statusText;

    private String levelText;

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getLevelText() {
        return levelText;
    }

    public void setLevelText(String levelText) {
        this.levelText = levelText;
    }
}
