package com.yuntao.zhushou.model.vo;

import java.io.Serializable;

/**
 * Created by shan on 2017/8/15.
 */
public class ActiveExecuteVo implements Serializable {

    private Long activeId;

    private boolean success;

    private String result;

    private String errMsg;

    public Long getActiveId() {
        return activeId;
    }

    public void setActiveId(Long activeId) {
        this.activeId = activeId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
