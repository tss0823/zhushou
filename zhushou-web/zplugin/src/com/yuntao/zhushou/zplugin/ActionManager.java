package com.yuntao.zhushou.zplugin;

import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.model.domain.codeBuild.DbConfigure;
import com.yuntao.zhushou.model.domain.codeBuild.Entity;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shan on 2017/9/8.
 */
public class ActionManager {

    public void newEntity(String projectPath, EntityParam entityParam) {

        ZpluginUtils.authCheck();

        //save entity
        Entity entity = new Entity();
        entity.setEnName(entityParam.getEnName());
        entity.setCnName(entityParam.getCnName());
        CodeBuildUtils.entitySave(entity);

        //save properties
        CodeBuildUtils.propertySave(entityParam);

        //getEntity
        ResponseObject entityResObj = CodeBuildUtils.getEntityByEnName(entityParam.getEnName());
        Map<String, Object> dataMap = (Map<String, Object>) entityResObj.getData();
        BeanUtils.mapToBean(dataMap, entity);

        //build sql
        ResponseObject responseObject = CodeBuildUtils.buildSql(entity.getId().toString());
        String sql = responseObject.getData().toString();

        //get dbConfigure
        responseObject = CodeBuildUtils.getDbConfigure();
        dataMap = (Map<String, Object>) responseObject.getData();
        DbConfigure dbConfigure = new DbConfigure();
        BeanUtils.mapToBean(dataMap, dbConfigure);
        JdbcUtils.execute(dbConfigure, sql);

        //build app
        responseObject = CodeBuildUtils.buildApp(entity.getId().toString());
        String downloadUrl = responseObject.getData().toString();

        //下载到临时目录
        RequestRes requestRes = new RequestRes();
        requestRes.setUrl(downloadUrl);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String tempPath = System.getProperty("java.io.tmpdir");
        String filePath = tempPath + "" + System.currentTimeMillis() + ".zip";
        File file = new File(filePath);
        try {
            FileUtils.writeByteArrayToFile(file, responseRes.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //然后解压到项目
        ZipUtil.explode(new File(filePath));

        //复制和替换文件到工作目录
        String outFilePath = filePath.substring(0, filePath.length() - 4);
        CodeSyncUtils.newSync(projectPath,outFilePath);

    }

    public static void main(String[] args) throws IOException {
        String projectPath = "/u01/workspace/fitness/";
        String filePath = "/private/var/folders/7r/bl7hlc351dg1vfpdclb7r7bc0000gn/T/1504864546908.zip";
        //复制和替换文件到工作目录
        String outFilePath = filePath.substring(0, filePath.length() - 4);
        String[] extensions = {"java", "xml"};
        Collection<File> files = FileUtils.listFiles(new File(outFilePath), extensions, true);
        for (File leafFile : files) {
            String newFilePath = leafFile.getPath().replace(outFilePath, projectPath);
            File newFile = new File(newFilePath);
            if (leafFile.getName().endsWith("DalConfig.java")) {  //提取字符复制
                String fileContent = FileUtils.readFileToString(leafFile);
                String pattern = "@Bean[^\\}]+\\}";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(fileContent);
                String preStr = null;
                StringBuilder sb = new StringBuilder();
                while (m.find()) {
                    if (preStr != null) {
                        sb.append("\t");
                        sb.append(preStr);
                        sb.append("\n");
                        sb.append("\n");
                    }
                    preStr = m.group();
                }

                List<String> readLines = FileUtils.readLines(newFile);
                StringBuilder dalSb = new StringBuilder();
                for (String readLine : readLines) {
                    if (StringUtils.contains(readLine, "<T> MapperFactoryBean<T>")) {
                        //add new content
                        dalSb.append(sb.toString());
                    }
                    dalSb.append(readLine);
                    dalSb.append("\n");
                }
                dalSb.delete(dalSb.length() - 1, dalSb.length());
                FileUtils.write(newFile, dalSb.toString());

            } else if (leafFile.getName().endsWith("mybatis-config.xml")) {  //提取字符复制
                String fileContent = FileUtils.readFileToString(leafFile);
                String pattern = "<typeAlias[^/>]+/>";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(fileContent);
                StringBuilder sbTypes = new StringBuilder();
                while (m.find()) {
                    sbTypes.append("\t\t");
                    sbTypes.append(m.group());
                    sbTypes.append("\n");
                }

                pattern = "<mapper [^>]+>";
                r = Pattern.compile(pattern);
                m = r.matcher(fileContent);
                StringBuilder sbMapper = new StringBuilder();
                while (m.find()) {
                    sbMapper.append("\t\t");
                    sbMapper.append(m.group());
                    sbMapper.append("\n");
                }

                List<String> readLines = FileUtils.readLines(newFile);
                StringBuilder xmlSb = new StringBuilder();
                for (String readLine : readLines) {
                    if (StringUtils.contains(readLine, "</typeAliases>")) {
                        //add new content
                        xmlSb.append(sbTypes.toString());
                    } else if (StringUtils.contains(readLine, "</mappers>")) {
                        //add new content
                        xmlSb.append(sbMapper.toString());
                    }
                    xmlSb.append(readLine);
                    xmlSb.append("\n");
                }
                xmlSb.delete(xmlSb.length() - 1, xmlSb.length());
                FileUtils.write(newFile, xmlSb.toString());

            } else {//直接复制

                if (StringUtils.contains(filePath, "/model/domain/")) { //自身跳过 TODO
                    continue;
                }
                FileUtils.copyFile(leafFile, newFile);
                System.out.println(newFilePath);

            }


        }
    }
}
