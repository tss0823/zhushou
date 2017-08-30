package com.yuntao.zhushou.model.vo.codeBuild;

import java.io.Serializable;

/**
 * Created by shan on 2017/8/29.
 */
public class ResultObj implements Serializable {

    private int result;

    private Object data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
