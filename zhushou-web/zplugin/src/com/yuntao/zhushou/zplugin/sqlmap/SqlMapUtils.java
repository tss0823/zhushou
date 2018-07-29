package com.yuntao.zhushou.zplugin.sqlmap;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import com.yuntao.zhushou.zplugin.AnalyseModelUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author: shengshan.tang
 * @date: 2018/7/28 下午9:49
 */
public class SqlMapUtils {


    public static EntityParam getEntityParamByclassType(String classTypeName) {
        //判断是否简称还是全名称
        String classAliasName = classTypeName;
        int lastIndex = classTypeName.lastIndexOf(".");
        if (lastIndex > 0) {
            //获取简称
             classAliasName = classTypeName.substring(lastIndex + 1);
        }
        EntityParam entityParam = SqlMapContextMgr.entityAliasMap.get(classAliasName);
        //找不到就去解析
        if(entityParam == null){
            String clsFullName = SqlMapContextMgr.entityNameAliasMap.get(classAliasName);
            //不在别名中
            if (StringUtils.isEmpty(clsFullName)) {
                //不是全程
                if(lastIndex < 0){
                    throw new RuntimeException("暂时支持处理 "+classAliasName);
                }
                clsFullName = classAliasName;
                SqlMapContextMgr.entityNameAliasMap.put(classAliasName,clsFullName);
            }
            //根据clsFullName 去解析
            String classTypeFilePath = SqlMapContextMgr.getProjectBasePath()+ File.separator+SqlMapContextMgr.getProjectName()+"-dal"+"/src/main/java/"+clsFullName.replaceAll("\\.","/")+".java";;
            VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(classTypeFilePath);
            PsiFile clsPsiFile = PsiManager.getInstance(SqlMapContextMgr.project).findFile(virtualFile);
            entityParam = AnalyseModelUtils.analysePropertyForXmlGen(clsPsiFile);
            entityParam.setClsFullName(clsFullName);
            SqlMapContextMgr.entityAliasMap.put(classAliasName,entityParam);
        }
        return entityParam;

    }
}
