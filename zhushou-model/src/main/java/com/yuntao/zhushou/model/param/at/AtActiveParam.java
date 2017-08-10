package com.yuntao.zhushou.model.param.at;

import com.yuntao.zhushou.model.domain.AtActive;
import com.yuntao.zhushou.model.domain.AtParameter;

import java.util.List;

/**
 * Created by shan on 2017/7/31.
 */
public class AtActiveParam extends AtActive {

    List<AtParameter> parameterList;

    public List<AtParameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<AtParameter> parameterList) {
        this.parameterList = parameterList;
    }
}
