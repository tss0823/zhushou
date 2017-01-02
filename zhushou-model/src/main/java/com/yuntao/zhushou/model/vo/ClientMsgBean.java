package com.yuntao.zhushou.model.vo;

import java.io.Serializable;

/**
 * Created by tangshengshan on 17-1-1.
 */
public class ClientMsgBean implements Serializable {

    private String id;

    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
