package com.yuntao.zhushou.model.param;

import com.yuntao.zhushou.model.domain.IdocParam;
import com.yuntao.zhushou.model.domain.IdocUrl;

import java.util.List;

/**
 * Created by shan on 2016/8/2.
 */
public class IdocDataParam extends IdocUrl {


    private List<IdocParam> paramList;



    public List<IdocParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<IdocParam> paramList) {
        this.paramList = paramList;
    }
}
