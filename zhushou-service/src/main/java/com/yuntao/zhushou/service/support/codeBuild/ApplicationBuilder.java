/**
 *
 */
package com.yuntao.zhushou.service.support.codeBuild;

import com.yuntao.zhushou.common.utils.*;
import com.yuntao.zhushou.model.domain.Attachment;
import com.yuntao.zhushou.model.domain.Property;
import com.yuntao.zhushou.model.domain.Template;
import com.yuntao.zhushou.model.enums.MysqlDataTypeEnum;
import com.yuntao.zhushou.model.vo.codeBuild.EntityBo;
import com.yuntao.zhushou.model.vo.codeBuild.ProjectBo;
import com.yuntao.zhushou.model.vo.codeBuild.PropertyBo;
import com.yuntao.zhushou.service.impl.AbstService;
import com.yuntao.zhushou.service.inter.AttachmentService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

//import org.apache.commons.io.FileUtils;

/**
 * 应用构建
 *
 * @author tangss
 * @2013年8月31日 @下午4:55:22
 */
@Component
public class ApplicationBuilder extends AbstService {

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 不用veclotiy渲染的模版后缀 集合
     */
    private final List<String> postfixList = new ArrayList<String>();

    /**
     * 单个生成文件目录集合
     */
//    private final List<String> singleFileModuleList = new ArrayList<String>();

    private final String udcodeStart = "user definition code start";
    private final String udcodeEnd = "user definition code end";

    {
        postfixList.add("vm");
        postfixList.add("css");
        postfixList.add("js");
        postfixList.add("png");
        postfixList.add("jpg");
        postfixList.add("gif");

//        String singlefileModule = ConfigUtils.getValue("gen.singlefileModule");
//        String sfmStrs[] = singlefileModule.split(",");
//        for (String sfm : sfmStrs) {
//            singleFileModuleList.add(sfm);
//        }
    }

    private Logger log = LoggerFactory.getLogger(ApplicationBuilder.class);

    private String getTemplatePath(Template template) {
        String templateDir = System.getProperty("java.io.tmpdir") + File.separator + template.getName()+File.separator+template.getGmtModify().getTime();
//        String templateDir = basePath + File.separator + templateFileName;
        File templateDirFile = new File(templateDir);
        if (!templateDirFile.exists()) { // 不存在解压
            Attachment attachment = attachmentService.findById(template.getAttachmentId());
            ZipUtils.unzipFile(attachment.getContent(), templateDir);
        }
        return templateDir;
    }

    private String getGenPath() {
        String gtime = DateUtil.getFmtyMdHmNoSymbol(System.currentTimeMillis());
        String genPath = System.getProperty("java.io.tmpdir") + gtime;
        return genPath;
    }

//    private Attachment genAttachment(String genPath) {
//        Attachment attachment = new Attachment();
//        String genZipPath = genPath + ".zip";
//        com.usefullc.platform.common.utils.FileUtils.zipFile(genPath, genZipPath, null);
//        byte[] content = null;
//        try {
//            File zipFile = new File(genZipPath);
//            attachment.setName(zipFile.getName());
//            content = FileUtils.readFileToByteArray(zipFile);
//            attachment.setContent(content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return attachment;
//    }

    public String buildApp(ProjectBo projectBo) {
        Map<String, Object> globalParamMap = new HashMap<String, Object>();
        Map<String, String> globalReplaceMap = new HashMap<String, String>();

//        String appShortName = projectBo.getsh();


        String templateDir = getTemplatePath(projectBo.getTemplate());
        String genPath = getGenPath();

        File templateDirFile = new File(templateDir);

        String time = DateUtil.getFmtYMDHM(System.currentTimeMillis());
        String packageName = projectBo.getPackageName();
        globalParamMap.put("time", time);
        globalParamMap.put("author", projectBo.getUserName());
        globalParamMap.put("packageName", packageName);
        globalParamMap.put("appEnName", projectBo.getEnName());
        String packagePath = packageName.replaceAll("\\.", "/");
        globalReplaceMap.put("packagePath", packagePath);
        globalReplaceMap.put("appEnName", projectBo.getEnName());
        globalParamMap.put("packagePath", packagePath);
//        globalParamMap.put("dbType", appBo.getDbConfigure().getType());
//        globalParamMap.put("tableSpace", appBo.getDbConfigure().getTableSpace());
        globalParamMap.put("jdbc", projectBo.getDbConfigure());


        List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
        List<Map<String, String>> replaceList = new ArrayList<Map<String, String>>();
        List<EntityBo> entityBoList = projectBo.getEntityBoList();
        for (EntityBo entityBo : entityBoList) {
            String entityCnName = entityBo.getCnName();
            String entityEnName = entityBo.getEnName();
            String upperEntityEnName = StringUtils.capitalize(entityEnName);
            String tableName = DbUtils.getTableName(entityBo.getTableName(),entityEnName);
//            String shortName = entityBo.getShortName();
//            if (StringUtils.isEmpty(shortName)) {
//                shortName = appShortName;
//            }
//            if (StringUtils.isNotEmpty(shortName)) {
//                tableName = shortName + "_" + tableName;
//            }
            entityBo.setUpperEntityEnName(upperEntityEnName);
            entityBo.setTableName(tableName);

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("bo", entityBo);
            paramMap.put("entityCnName", entityCnName);
            paramMap.put("entityEnName", entityEnName);
            paramMap.put("upperEntityEnName", upperEntityEnName);
            paramMap.put("tableName", tableName);
            paramMap.put("upperTableName", tableName.toUpperCase());

            // replace目录文件用
            Map<String, String> replaceMap = new HashMap<String, String>();
            replaceMap.put("entityEnName", entityEnName);
            replaceMap.put("entityCnName", entityCnName);
            replaceMap.put("upperEntityEnName", upperEntityEnName);
            replaceMap.putAll(globalReplaceMap);
            replaceList.add(replaceMap);

            List<PropertyBo> propBoList = entityBo.getPropList();
            for (PropertyBo propertyBo : propBoList) {
                // String columnType = DbTypeMysqlUtils.getValue(propertyBo.getDataType());
                String enName = propertyBo.getEnName();
                String upperEnName = StringUtils.capitalize(enName);
                String columnName = DbUtils.javaToTableName(enName);
                // propertyBo.setColumnType(columnType);
                propertyBo.setUpperEnName(upperEnName);
                propertyBo.setColumnName(columnName);
//                propertyBo.setUpperColumnName(columnName.toUpperCase());
            }
            //modify at 2016-08-4
            globalParamMap.put("entityBoList", entityBoList);  //为了mybatis-config dalConfig 能拿到实体

            paramMap.putAll(globalParamMap);
            paramList.add(paramMap);

        }

        // 构建multi
        for (int i = 0; i < paramList.size(); i++) {
            Map<String, Object> paramMap = paramList.get(i);
            String multiPath = "";  //ConfigUtils.getValue("gen.multiPath");
            String abstractMultiPath = templateDir + File.separator + multiPath;
            try {
                File multiTemplateFile = new File(abstractMultiPath);
                //templateDir= 模板路径
                //genPath=生成径路
                operateFile(templateDir, paramMap, replaceList.get(i), multiTemplateFile, genPath, true);
            } catch (IOException e) {
                throw new RuntimeException("operateFile failed! ", e);
            }
        }
        // 构建single
//        globalParamMap.put("appBo", appBo);
        try {
            operateFile(templateDir, globalParamMap, globalReplaceMap, templateDirFile, genPath, false);
        } catch (IOException e) {
            throw new RuntimeException("operateFile failed! ", e);
        }
        // 返回
        String genZipPath = genPath + ".zip";
        ZipUtils.zipFile(genPath, genZipPath, null);
//        try {
//            Process process = Runtime.getRuntime().exec("zip -r " + genZipPath + " " + genPath);
////            InputStream inputStream = process.getInputStream();
////            List<String> strings = IOUtils.readLines(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return genZipPath;
//        return genAttachment(genPath);

    }


    private void operateFile(String baseTemplateDir, Map<String, Object> paramMap, Map<String, String> replaceMap,
                             File dirFile, String genPath, boolean isMulti) throws IOException {
        File[] files = dirFile.listFiles();
        if (files == null || files.length == 0) { // 目录下没有文件
            String filePath = dirFile.getPath();
            String oppsitePath = filePath.substring(baseTemplateDir.length());
            log.info("oppsitePath=" + oppsitePath);
            String newFilePath = genPath + File.separator + oppsitePath;
            newFilePath = ReplaceUtils.replaceAll(newFilePath, replaceMap);
            log.info("newFilePath=" + newFilePath);
            new File(newFilePath).mkdirs();
        }
        for (File file : files) {
            String fileName = file.getName();
            String filePath = file.getPath();
            String templateDir = file.getParent();
            String oppsitePath = filePath.substring(baseTemplateDir.length());
            if (file.isFile()) { // 文件处理
                // 防止人工错误
                if (isMulti && fileName.indexOf("_") == -1) { // 多个规则，遇到非动态字符则过滤
                    continue;
                } else if (!isMulti && fileName.indexOf("_") != -1) { // 单个规则，遇到动态字符则过滤
                    continue;
                }
                log.info("oppsitePath=" + oppsitePath);
                String newFilePath = genPath + File.separator + oppsitePath;
                newFilePath = ReplaceUtils.replaceAll(newFilePath, replaceMap);
                log.info("newFilePath=" + newFilePath);
                File newFile = new File(newFilePath);
                // 后缀过滤，不解析，直接复制内容
                String extension = FilenameUtils.getExtension(fileName);
                if (postfixList.contains(extension)) {
                    byte[] byteArray = FileUtils.readFileToByteArray(file);
                    FileUtils.writeByteArrayToFile(newFile, byteArray);
                } else {
                    // velocity模版解析
                    String parentPath = file.getParent();
//                    int start = parentPath.lastIndexOf(File.separator);
//                    int end = parentPath.length();
//                    String fileModule = filePath.substring(start + 1, end);
//                    if (hasHis && !singleFileModuleList.contains(fileModule)) {
//                        continue;
//                    }

                    if (fileName.endsWith("mybatis-config.xml") || fileName.endsWith("DalConfig.java")) {
                        System.out.printf("");
                    }

                    String fileContent = VelocityRenderUtils.getContent(paramMap, templateDir, file.getName());
                    // 对于mapper文件特殊处理
                    if (fileName.endsWith("Mapper.xml")) {
                        StringTokenizer st = new StringTokenizer(fileContent, "\n");
                        String lineText = null;
                        StringBuilder sb = new StringBuilder();
                        while (st.hasMoreElements()) {
                            lineText = st.nextToken();
                            sb.append(lineText);
                            sb.append("\n");
                            // 遇到开始自定义code就添加老文件用户自定义code
                            if (StringUtils.indexOf(lineText, udcodeStart) != -1) {
                                String udCode = getMapperUdCode(newFile);
                                if (StringUtils.isNotEmpty(udCode)) {
                                    sb.append(udCode);
                                    sb.append("\n");
                                }
                            }
                        }
                        fileContent = sb.toString();
                    }
                    FileUtils.writeStringToFile(newFile, fileContent, "utf-8");
                }
            } else { // 目录处理
                operateFile(baseTemplateDir, paramMap, replaceMap, file, genPath, isMulti);
            }
        }
    }

    private String getMapperUdCode(File file) {
        String fileContent = null;
        if (!file.exists()) {
            return "";
        }
        try {
            fileContent = FileUtils.readFileToString(file, "utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        StringTokenizer st = new StringTokenizer(fileContent, "\n");
        String lineText = null;
        boolean isStart = false;
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreElements()) {
            lineText = st.nextToken();
            if (StringUtils.indexOf(lineText, udcodeEnd) != -1) {
                break;
            }
            if (isStart && !StringUtils.equals(StringUtils.trim(lineText), "\t")) {
                sb.append(lineText);
                sb.append("\n");
            }
            if (StringUtils.indexOf(lineText, udcodeStart) != -1) {
                isStart = true;
            }
        }
        return sb.toString();
    }

    private void clearFoder(String clearFilePath) throws IOException {
        // FileUtils.deleteDirectory(new File(clearFilePath));
    }

    public String buildTableSql(EntityBo entity, List<PropertyBo> propList) {
        String sql = "";
        StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS `");
//        String shortName = appBo.getShortName();
//        if (!org.springframework.util.StringUtils.isEmpty(entity.getShortName())) {
//            shortName = entity.getShortName();
//        }
        String tableName = DbUtils.getTableName(entity.getTableName(),entity.getEnName());
//        if (!org.springframework.util.StringUtils.isEmpty(shortName)) {
//            tableName = shortName + "_" + tableName;
//        }
        sb.append(tableName);
        sb.append("`;");
        sb.append("\n");
        sb.append("CREATE TABLE `");

        sb.append(tableName);
        sb.append("` (");
        String primaryKey = null;
        for (Property prop : propList) {
            sb.append("\n  ");
            sb.append("`");
            String columnName = DbUtils.javaToTableName(prop.getEnName());
            sb.append(columnName);
            sb.append("`");
            sb.append(" ");
            String columnType = DbTypeMysqlUtils.getValue(prop.getDataType());
            sb.append(columnType);
            if (!columnType.equals(MysqlDataTypeEnum.TIME.getDbValue())) {
                sb.append("(");
                if (org.springframework.util.StringUtils.isEmpty(prop.getLength()) && columnType.equals("tinyint")) {
                    sb.append(4);
                } else {
                    sb.append(prop.getLength());
                }
                sb.append(")");
            }
            sb.append(" ");
            if (prop.getIsNull() != null && !prop.getIsNull()) {
                sb.append("NOT NULL");
                sb.append(" ");
            } else {
                sb.append("DEFAULT");
                sb.append(" ");
                if (org.springframework.util.StringUtils.isEmpty(prop.getDefaultValue())) {
                    sb.append("NULL");
                } else {
                    sb.append(prop.getDefaultValue());
                }
            }
            if (prop.getPrimaryKey()) {
                sb.append("AUTO_INCREMENT");
                primaryKey = columnName;
            }
            sb.append(" ");
            sb.append("COMMENT");
            sb.append(" ");
            sb.append("'");
            sb.append(prop.getCnName());
            sb.append("'");
            sb.append(",");
        }
        sb.append("\n");
        sb.append("  PRIMARY KEY (`");
        sb.append(primaryKey);
        sb.append("`)");
        sb.append("\n");
        sb.append(")");
        sb.append(" ");
        sb.append("ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='");
        sb.append(entity.getCnName());
        sb.append("';");
        sb.append("\n\n");
        sql = sb.toString();
        return sql;
    }

    public static void main(String[] args) {
        ZipUtils.zipFile("/var/folders/vc/sxkyjmp92hv16x3vxysqr4jc0000gn/T/20180714233537", "/var/folders/vc/sxkyjmp92hv16x3vxysqr4jc0000gn/T/20180714233537.zip", null);
    }
}
