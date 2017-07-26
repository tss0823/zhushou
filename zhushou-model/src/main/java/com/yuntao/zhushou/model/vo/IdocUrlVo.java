package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.IdocParam;
import com.yuntao.zhushou.model.domain.IdocUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口主体
 * 
 * @author admin
 *
 * @2016-07-30 20
 */
public class IdocUrlVo extends IdocUrl {

    private List<IdocParam> paramList = new ArrayList<IdocParam>();

    private String lastTime;



    public List<IdocParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<IdocParam> paramList) {
        this.paramList = paramList;
    }

    public void addParam(IdocParamVo idocParamVo) {
        this.paramList.add(idocParamVo);
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

}
