package com.yuntao.zhushou.zplugin.sqlmap;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import com.yuntao.zhushou.zplugin.AnalyseModelUtils;
import com.yuntao.zhushou.zplugin.sqlmap.bean.SqlMapMethod;
import com.yuntao.zhushou.zplugin.sqlmap.bean.SqlMapParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        if (entityParam == null) {
            String clsFullName = SqlMapContextMgr.entityNameAliasMap.get(classAliasName);
            //不在别名中
            if (StringUtils.isEmpty(clsFullName)) {
                //不是全程
                if (lastIndex < 0) {
                    throw new RuntimeException("暂时支持处理 " + classAliasName);
                }
                clsFullName = classAliasName;
                SqlMapContextMgr.entityNameAliasMap.put(classAliasName, clsFullName);
            }
            //根据clsFullName 去解析
            String classTypeFilePath = SqlMapContextMgr.getProjectBasePath() + File.separator + SqlMapContextMgr.getProjectName() + "-dal" + "/src/main/java/" + clsFullName.replaceAll("\\.", "/") + ".java";
            ;
            VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(classTypeFilePath);
            PsiFile clsPsiFile = PsiManager.getInstance(SqlMapContextMgr.project).findFile(virtualFile);
            entityParam = AnalyseModelUtils.analysePropertyForXmlGen(clsPsiFile);
            entityParam.setClsFullName(clsFullName);
            SqlMapContextMgr.entityAliasMap.put(classAliasName, entityParam);
        }
        return entityParam;

    }

    public static String getShortTypeName(String javaType){
        int index = javaType.lastIndexOf(".");
        if(index > 0){
            javaType = javaType.substring(index+1);
        }
        return javaType;

    }

    public static void genSqlMapJavaMethod(SqlMapMethod sqlMapMethod) {
        Map<String,Integer> treeMap = new TreeMap<>();
        treeMap.put("Dao",0);
//        treeMap.put("Manager",1);
        treeMap.put("Service",1);
        treeMap.put("client",2);
        //生成mapper
        String mapperFilePath = SqlMapContextMgr.getProjectBasePath() + File.separator + SqlMapContextMgr.getProjectName() + "-dal" + "/src/main/java/" +
                sqlMapMethod.getPackageName().replaceAll("\\.", "/") + "/dal/mapper/" + sqlMapMethod.getAliasName() + "Mapper.java";

        StringBuilder sb = new StringBuilder();
        sb.append("\n\t");
        sb.append(sqlMapMethod.getReturnType());
        sb.append(" ");
        sb.append(sqlMapMethod.getName());
        sb.append("(");
        List<SqlMapParam> sqlMapParamList = sqlMapMethod.getSqlMapParamList();
        if (CollectionUtils.isNotEmpty(sqlMapParamList)) {
            boolean first = true;
            for (SqlMapParam sqlMapParam : sqlMapParamList) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append("@Param(\"");
                sb.append(sqlMapParam.getName());
                sb.append("\")");
                sb.append(" ");
                sb.append(sqlMapParam.getType());
                sb.append(" ");
                sb.append(sqlMapParam.getName());
            }

        }
        sb.append(");");
        try {
            File mapperFile = new File(mapperFilePath);
            List<String> strings = FileUtils.readLines(mapperFile);
            for (int i = strings.size() - 1; i >= 0; i--) {
                if(strings.contains("}")){
                    strings.remove(i);
                    break;
                }
            }
            strings.add(sb.toString());
            strings.add("\n}");

            FileUtils.writeLines(mapperFile,strings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        int batchUpdateBrand(@Param("ids") List<Long> ids,@Param("brandId") Long brandId,@Param("brandName") String brandName);

        Integer integer = treeMap.get(sqlMapMethod.getGenScope());
        if(integer == null){
            return;
        }

        String daoFilePath = SqlMapContextMgr.getProjectBasePath() + File.separator + SqlMapContextMgr.getProjectName() + "-dal" + "/src/main/java/" +
                sqlMapMethod.getPackageName().replaceAll("\\.", "/") + "/dal/dao/" + sqlMapMethod.getAliasName() + "Dao.java";
        sb = new StringBuilder();
        sb.append("\n\t");
        sb.append(sqlMapMethod.getReturnType());
        sb.append(" ");
        sb.append(sqlMapMethod.getName());
        sb.append("(");
        if (CollectionUtils.isNotEmpty(sqlMapParamList)) {
            boolean first = true;
            for (SqlMapParam sqlMapParam : sqlMapParamList) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(sqlMapParam.getType());
                sb.append(" ");
                sb.append(sqlMapParam.getName());
            }

        }
        sb.append(");");
        try {
            File file = new File(daoFilePath);
            List<String> strings = FileUtils.readLines(file);
            for (int i = strings.size() - 1; i >= 0; i--) {
                if(strings.contains("}")){
                    strings.remove(i);
                    break;
                }
            }
            strings.add(sb.toString());
            strings.add("\n}");

            FileUtils.writeLines(file,strings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String daoImplFilePath = SqlMapContextMgr.getProjectBasePath() + File.separator + SqlMapContextMgr.getProjectName() + "-dal" + "/src/main/java/" +
                sqlMapMethod.getPackageName().replaceAll("\\.", "/") + "/dal/dao/impl/" + sqlMapMethod.getAliasName() + "DaoImpl.java";

        sb = new StringBuilder();
        sb.append("\n\t");
        sb.append("@Override");
        sb.append("\n\t");
        sb.append("public ");
        sb.append(sqlMapMethod.getReturnType());
        sb.append(" ");
        sb.append(sqlMapMethod.getName());
        sb.append("(");
        if (CollectionUtils.isNotEmpty(sqlMapParamList)) {
            boolean first = true;
            for (SqlMapParam sqlMapParam : sqlMapParamList) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(sqlMapParam.getType());
                sb.append(" ");
                sb.append(sqlMapParam.getName());
            }

        }
        sb.append(") {\n");
        sb.append("\t\t");
        sb.append("return "+StringUtils.uncapitalize(sqlMapMethod.getAliasName())+"Mapper."+sqlMapMethod.getName()+"(");
        if (CollectionUtils.isNotEmpty(sqlMapParamList)) {
            boolean first = true;
            for (SqlMapParam sqlMapParam : sqlMapParamList) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(sqlMapParam.getName());
            }
        }
        sb.append(");");
        sb.append("\n\t}");
        try {
            File file = new File(daoImplFilePath);
            List<String> strings = FileUtils.readLines(file);
            for (int i = strings.size() - 1; i >= 0; i--) {
                if(strings.contains("}")){
                    strings.remove(i);
                    break;
                }
            }
            strings.add(sb.toString());
            strings.add("\n}");

            FileUtils.writeLines(file,strings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String serviceFilePath = SqlMapContextMgr.getProjectBasePath() + File.separator + SqlMapContextMgr.getProjectName() + "-common" + "/src/main/java/" +
                sqlMapMethod.getPackageName().replaceAll("\\.", "/") + "/common/service" + sqlMapMethod.getAliasName() + "Service.java";
        String serviceImplFilePath = SqlMapContextMgr.getProjectBasePath() + File.separator + SqlMapContextMgr.getProjectName() + "-core" + "/src/main/java/" +
                sqlMapMethod.getPackageName().replaceAll("\\.", "/") + "/core/service" + sqlMapMethod.getAliasName() + "ServiceImpl.java";

        String clientFilePath = SqlMapContextMgr.getProjectBasePath() + File.separator + SqlMapContextMgr.getProjectName() + "-client" + "/src/main/java/" +
                sqlMapMethod.getPackageName().replaceAll("\\.", "/") + "/client/service/" + sqlMapMethod.getAliasName() + "Client.java";


    }
}
