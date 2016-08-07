package com.yuntao.zhushou.model.vo;

import com.yuntao.zhushou.model.domain.AuthRes;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限资源
 * 
 * @author admin
 *
 * @2016-08-07 11
 */
public class AuthResVo extends AuthRes {

    private List<AuthRes> childList = new ArrayList<>();

    public List<AuthRes> getChildList() {
        return childList;
    }

    public void addChild(AuthRes child) {
        this.childList.add(child);
    }
}
