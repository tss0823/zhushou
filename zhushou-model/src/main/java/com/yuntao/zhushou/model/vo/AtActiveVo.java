package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.AtActive;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动模板
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
public class AtActiveVo extends AtActive {

    List<AtParameterVo> parameterVoList = new ArrayList<>();


    public List<AtParameterVo> getParameterVoList() {
        return parameterVoList;
    }

    public void setParameterVoList(List<AtParameterVo> parameterVoList) {
        this.parameterVoList = parameterVoList;
    }
}
