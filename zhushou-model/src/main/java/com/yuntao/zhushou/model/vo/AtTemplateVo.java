package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.AtActive;
import com.yuntao.zhushou.model.domain.AtTemplate;

import java.util.List;

/**
 * 测试模板
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
public class AtTemplateVo extends AtTemplate {

    private String month;

    private List<AtActiveVo> activeVoList;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<AtActiveVo> getActiveVoList() {
        return activeVoList;
    }

    public void setActiveVoList(List<AtActiveVo> activeVoList) {
        this.activeVoList = activeVoList;
    }
}
