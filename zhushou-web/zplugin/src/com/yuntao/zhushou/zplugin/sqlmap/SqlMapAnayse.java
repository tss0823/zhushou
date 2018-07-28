package com.yuntao.zhushou.zplugin.sqlmap;

import com.yuntao.zhushou.model.domain.Property;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import com.yuntao.zhushou.zplugin.sqlmap.bean.SqlMapMethod;
import com.yuntao.zhushou.zplugin.sqlmap.bean.SqlMapParam;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: shengshan.tang
 * @date: 2018/7/15 下午10:17
 */
public class SqlMapAnayse {

    private static Map<String, String> resultMap = new HashMap<>();

    static {
        resultMap.put("insert", "int");
        resultMap.put("update", "int");
        resultMap.put("delete", "int");
        resultMap.put("select", "List");
    }

    public static SqlMapMethod anayse(String text,String currentAlias ,Map<String, EntityParam> aliasEntityMap) {
        SqlMapMethod sqlMapMethod = new SqlMapMethod();
        EntityParam currentEntityParam = aliasEntityMap.get(currentAlias);
        try {
            //创建解析器
            SAXReader reader = new SAXReader();
            InputStream inputStream = new ByteArrayInputStream(text.getBytes());
            Document document = reader.read(inputStream);
            Element element = document.getRootElement();
            String tagName = element.getName();
            String resultValue = resultMap.get(tagName);
            if (StringUtils.isEmpty(resultValue)) {
                throw new RuntimeException("sqlmap 必须是 " + resultMap.keySet().toString());
            }
            String id = element.attributeValue("id");
            sqlMapMethod.setName(id);

            //参数处理 TODO parameterMap
            String parameterType = element.attributeValue("parameterType");
            if (StringUtils.isNotEmpty(parameterType)) {  //参数
                String parameterTypeAlias = parameterType;
                //判断是否简称还是全名称
                int lastIndex = parameterType.lastIndexOf(".");
                if (lastIndex > 0) {
                    //获取简称
                    parameterTypeAlias = parameterType.substring(lastIndex + 1);
                }
                EntityParam entityParam = aliasEntityMap.get(parameterTypeAlias);
                if (entityParam == null) {
                    throw new RuntimeException("暂不支持处理 " + parameterType);
                }
                SqlMapParam sqlMapParam = new SqlMapParam(parameterTypeAlias, StringUtils.uncapitalize(parameterTypeAlias));
                sqlMapMethod.addSqlMapParam(sqlMapParam);
                sqlMapMethod.addImportCls(entityParam.getClsFullName());

            } else {  //没有设置参数

            }
            //
            String textContent = element.getText();
            List<String> paramList = new ArrayList<>();


            //获取参数
            String pattern = "\\#\\{[^\\}]+\\}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(textContent);
            while (m.find()) {
                String group = m.group();
                String paramName = group.substring(2, group.length() - 1);
                if (paramList.contains(paramName)) {
                    continue;
                }
                //match param
                List<Property> propertyList = currentEntityParam.getPropertyList();
                for (Property property : propertyList) {
                    if (property.getEnName().equals(paramName)) {

                    }
                }

                sqlMapMethod.addSqlMapParam(paramName);
            }

            //返回参数处理
            if (StringUtils.equals(tagName, "select")) {
                String resultType = element.attributeValue("resultType");
                String resultMap = element.attributeValue("resultMap");
                if (StringUtils.isNotEmpty(resultType)) {  //一般select 必须存在 resultType
                    String resultTypeAlias = resultType;
                    //判断是否简称还是全名称
                    int lastIndex = resultType.lastIndexOf(".");
                    if (lastIndex > 0) {
                        //获取简称
                        resultTypeAlias = resultType.substring(lastIndex + 1);
                    }
                    EntityParam entityParam = aliasEntityMap.get(resultTypeAlias);
                    if (entityParam == null) {
                        throw new RuntimeException("暂不支持处理 " + parameterType);
                    }
                    sqlMapMethod.setReturnType(resultTypeAlias);
                    sqlMapMethod.addImportCls(entityParam.getClsFullName());
                } else if (StringUtils.isNotEmpty(resultMap)) {  //一般select 必须存在 resultMap
                    if(resultMap.equals("BaseResultMap")){  //暂时这么处理，后需要反向找到resultMap type
                        sqlMapMethod.setReturnType(currentAlias);
                        sqlMapMethod.addImportCls(currentEntityParam.getClsFullName());
                    }
                } else {  //非查询
                    throw new RuntimeException("select must exist resultType or resultMap");
                }
                //处理返回object or list
                if(!sqlMapMethod.getName().startsWith("get") && !sqlMapMethod.getName().startsWith("find")){  //list
                    sqlMapMethod.setReturnType("List<"+sqlMapMethod.getReturnType()+">");
                    sqlMapMethod.addImportCls("java.lang.List");
                }
            } else {
                sqlMapMethod.setReturnType(resultValue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlMapMethod;
    }

    public static Map<String, String> anayseMyBatisConfig(InputStream inputStream) {
        Map<String, String> map = new HashMap<>();
        try {
            //创建解析器
            SAXReader reader = new SAXReader();
//            System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
            Document document = reader.read(inputStream);
            Element root = document.getRootElement();
            List<Node> typeAlias = root.selectNodes("typeAliases/typeAlias");

            for (Node node : typeAlias) {
                String alias = node.valueOf("@alias");
                String type = node.valueOf("@type");
                map.put(alias,type);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, String> anayseBeanPath(String parentPath, Map<String, String> aliasBeanMap) {
        Set<Map.Entry<String, String>> entries = aliasBeanMap.entrySet();
        Map<String, String> clssPathMap = new HashMap<>();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            String clsPath = parentPath + File.separator + value.replaceAll("\\.", File.separator )+".java";
            clssPathMap.put(key, clsPath);


        }
        return clssPathMap;
    }

    public static void main2(String[] args) {
        String pattern = "\\#\\{[^\\}]+\\}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher("abdf#{name}sfdfs=32#{age}");
        while (m.find()) {
            String group = m.group();
            String paramName = group.substring(2, group.length() - 1);
            System.out.println(paramName);
        }
    }


    public static void main(String[] args) throws IOException {

//        List<String> strings = IOUtils.readLines();
//        File configFile = new File("/Users/pro/workspace/darwin/darwin-dal/src/main/resources/mybatis-config.xml");
        FileInputStream fileInputStream = new FileInputStream(new File("/Users/pro/workspace/darwin/darwin-dal/src/main/resources/mybatis-config.xml"));
        Map<String, String> stringStringMap = anayseMyBatisConfig(fileInputStream);
        System.out.println(stringStringMap);

    }
}
