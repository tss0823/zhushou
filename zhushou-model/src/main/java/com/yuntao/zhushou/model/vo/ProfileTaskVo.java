/*
 * Copyright 2010-2011 ESunny.com All right reserved. This software is the confidential and proprietary information of
 * ESunny.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with ESunny.com.
 */
package com.yuntao.zhushou.model.vo;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * 类ProfileTask.java的实现描述：性能统计监控任务
 * 
 * @author shengshang.tang 2014年5月19日 下午4:25:17
 */
public class ProfileTaskVo implements Serializable {

    private String key;

    /**
     * 日志级别
     */
    private String level;

    /**
     * 开始时间
     */
    private Long   startTime;

    /**
     * 结束时间
     */
    private Long   endTime;

    /**
     * 开销时间
     */
    private Long   time;

    /**
     * 名称(url请求链路中必须是唯一的)
     */
    private String name;

    /**
     * 内容
     */
    private String content;

    private List<ProfileTaskVo> childList;

    public ProfileTaskVo() {
        this.key = UUID.randomUUID().toString();
    }

    public Long getTime() {
        time = endTime - startTime;
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<ProfileTaskVo> getChildList() {
        return childList;
    }

    public void setChildList(List<ProfileTaskVo> childList) {
        this.childList = childList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
