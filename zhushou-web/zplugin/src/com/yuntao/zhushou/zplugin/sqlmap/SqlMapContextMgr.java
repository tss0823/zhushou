package com.yuntao.zhushou.zplugin.sqlmap;

import com.intellij.openapi.project.Project;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: shengshan.tang
 * @date: 2018/7/28 下午9:54
 */
public class SqlMapContextMgr {

    public static Map<String,String> entityNameAliasMap;

    public static Map<String,EntityParam> entityAliasMap = new HashMap<>();

    public static String getProjectName () {
        if(project != null){
            return project.getName();
        }else{
           return null;
        }
    }

    public static String getProjectBasePath (){
        if(project != null){
            return project.getBasePath();
        }else{
            return null;
        }
    }

    public static Project project = null;

}
