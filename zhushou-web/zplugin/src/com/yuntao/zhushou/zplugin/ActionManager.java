package com.yuntao.zhushou.zplugin;

import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.model.domain.Entity;
import com.yuntao.zhushou.model.domain.Property;
import com.yuntao.zhushou.model.domain.codeBuild.DbConfigure;
import com.yuntao.zhushou.model.enums.MysqlDataTypeEnum;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yuntao.zhushou.zplugin.ZhushouRpcUtils.getLoginCookie;

/**
 * Created by shan on 2017/9/8.
 */
public class ActionManager {
    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");
    protected final static Logger log = org.slf4j.LoggerFactory.getLogger(ActionManager.class);
    /**
     * @param action      0 newEntity,1 addProperty 2 delProperty 3 delEntity
     * @param entityParam
     * @return
     */
    private String build(int action, EntityParam entityParam) {
        ZpluginUtils.authCheck();

        //getEntity
//        Entity entity = new Entity();
        ResponseObject entityResObj = ZhushouRpcUtils.getEntityByEnName(entityParam.getEnName());
        Map<String, Object> dataMap = (Map<String, Object>) entityResObj.getData();
        Entity entity = (Entity) BeanUtils.mapToBean(dataMap, Entity.class);

        //save entity
        entity.setEnName(entityParam.getEnName());
        entity.setCnName(entityParam.getCnName());
        List<Property> editPropertyList = null;
        List<Property> selectPropertyList = entityParam.getPropertyList();
        if (action == 0) {  //保存实体
            if(entity.getId() != null){
                throw new BizException("已经存在实体，无需新建");
            }
            ZhushouRpcUtils.entitySave(entity);

            //再获取一次
            entityResObj = ZhushouRpcUtils.getEntityByEnName(entityParam.getEnName());
            dataMap = (Map<String, Object>) entityResObj.getData();
            entity = (Entity) BeanUtils.mapToBean(dataMap, Entity.class);

            editPropertyList = selectPropertyList;
        }else if(action == 1){  //添加属性
            if(entity.getId() == null){
                throw new BizException("实体不存在，请先新建");
            }
            editPropertyList = ZhushouRpcUtils.propertyList(entity.getId());
            for (Property property : selectPropertyList) {
                editPropertyList.add(property);
            }
        }else if(action == 2) {
            if(entity.getId() == null){
                throw new BizException("实体不存在，请先新建");
            }
            editPropertyList = ZhushouRpcUtils.propertyList(entity.getId());
            for (Property selectProperty : selectPropertyList) {
                for (Property editProperty : editPropertyList) {
                    if(selectProperty.getEnName().equals(editProperty.getEnName())){
                        editPropertyList.remove(editProperty);
                        break;
                    }
                }
            }

        }else if(action == 3){
            if(entity.getId() == null){
                throw new BizException("实体不存在，请先新建");
            }
        }


        //save properties
        entityParam.setId(entity.getId());
        if(action != 3){
            ZhushouRpcUtils.propertySave(entity.getId(),editPropertyList);
        }


        //build sql
//        List<Property> propertyList = entityParam.getPropertyList();
        String sql = null;
        if (action == 0) {
            ResponseObject responseObject = ZhushouRpcUtils.buildSql(entity.getId().toString());
            sql = responseObject.getData().toString();

        } else if (action == 1) {
            StringBuilder sb = new StringBuilder();
            for (Property property : selectPropertyList) {
                sb.append("ALTER TABLE `" + entityParam.getEnName() + "` ADD `" + property.getEnName() + "`");
                String dataType = property.getDataType();
                String dbType = MysqlDataTypeEnum.getDbValueByJavaValue(dataType);
                sb.append(" "+dbType);
                if (StringUtils.isNotEmpty(property.getLength())) {
                    sb.append("("+property.getLength()+")");
                }
                if(property.getIsNull()){
                    sb.append(" NULL");
                }else{
                    sb.append(" NOT NULL");
                }
                if(StringUtils.isNotEmpty(property.getDefaultValue())){
                    sb.append(" DEFAULT ");
                    if(property.getDataType().endsWith("String")){
                        sb.append("'"+property.getDefaultValue()+"'");
                    }else{
                        sb.append(property.getDefaultValue());
                    }
                }
                sb.append(" COMMENT '"+property.getCnName() +"';");
            }
            sql = sb.toString();
        }else if(action == 2){
            StringBuilder sb = new StringBuilder();
            for (Property property : selectPropertyList) {
                sb.append("ALTER TABLE `"+entityParam.getEnName()+"` DROP `"+property.getEnName()+"`;");
            }
            sql = sb.toString();
        }else if(action == 3){
            sql = "DROP TABLE `"+entityParam.getEnName()+"`;";
        }


        //get dbConfigure
        ResponseObject responseObject = ZhushouRpcUtils.getDbConfigure();
        dataMap = (Map<String, Object>) responseObject.getData();
        DbConfigure dbConfigure = new DbConfigure();
        BeanUtils.mapToBean(dataMap, dbConfigure);
        JdbcUtils.execute(dbConfigure, sql);
        ZhushouRpcUtils.buildSqlSave(sql);
        bisLog.info("execute sql >>>");
        bisLog.info(sql);

        //build app
        String outFilePath = null;
        if(action != 2){  //删除property 不需要
            responseObject = ZhushouRpcUtils.buildApp(entity.getId().toString());
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
            outFilePath = filePath.substring(0, filePath.length() - 4);
            ZipUtil.unpack(new File(filePath), new File(outFilePath));
        }

        if(action == 3){  //删除实体最后操作
            ZhushouRpcUtils.entityDelete(entity.getId());
        }
        return outFilePath;
    }


    public void newEntity(String projectPath, EntityParam entityParam) {
        String outFilePath = this.build(0, entityParam);

        //复制和替换文件到工作目录
        CodeSyncUtils.newSync(projectPath, outFilePath);

    }

    public void addProperty(String projectPath, EntityParam entityParam) {
        String outFilePath = this.build(1, entityParam);

        //复制和替换文件到工作目录
        CodeSyncUtils.updateSync(projectPath, outFilePath, entityParam);
    }

    public void delProperty(String projectPath, EntityParam entityParam) {
        this.build(2, entityParam);

        //复制和替换文件到工作目录
        CodeSyncUtils.delPropertySync(projectPath,  entityParam);
    }

    public void delEntity(String projectPath, EntityParam entityParam) {
        String outFilePath = this.build(3, entityParam);

        //复制和替换文件到工作目录
        CodeSyncUtils.delEntitySync(projectPath, outFilePath,entityParam.getEnName());
    }

    public ResponseObject deployTest(){
        String testBranch = ZpluginUtils.getTestBranch();
        if (StringUtils.isEmpty(testBranch)) {
            throw new BizException("请先设置测试分支");
        }
        ZpluginUtils.authCheck();

        RequestRes requestRes = new RequestRes();
        requestRes.setUrl(ZpluginConstant.zhushouUrl+"deploy/compileAndDeploy");
        //        headers
        Map<String,String> headerMap = new HashMap<>();
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(getLoginCookie())){
            headerMap.put("Cookie",getLoginCookie());
        }
        requestRes.setHeaders(headerMap);
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appName","member");
        paramMap.put("branch",testBranch);
        paramMap.put("model","test");
        requestRes.setParams(paramMap);
        ResponseRes responseRes = HttpNewUtils.execute(requestRes);
        String bodyText = responseRes.getBodyText();
        if(responseRes.getStatus() == 200){
            ResponseObject responseObject = JsonUtils.json2Object(bodyText, ResponseObject.class);
            return responseObject;
        }else{
            throw new BizException(bodyText);
        }
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
