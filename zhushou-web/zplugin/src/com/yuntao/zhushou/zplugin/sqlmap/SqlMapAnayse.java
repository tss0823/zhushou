package com.yuntao.zhushou.zplugin.sqlmap;

import com.intellij.ide.ui.EditorOptionsTopHitProvider;
import com.yuntao.zhushou.model.domain.Property;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import com.yuntao.zhushou.zplugin.sqlmap.bean.SqlMapMethod;
import com.yuntao.zhushou.zplugin.sqlmap.bean.SqlMapParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.dom4j.Document;
import org.dom4j.DocumentException;
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

    public static SqlMapMethod anayse(String text, String currentAlias) {
        SqlMapMethod sqlMapMethod = new SqlMapMethod();
        EntityParam currentEntityParam = SqlMapUtils.getEntityParamByclassType(currentAlias);
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
            String parameterMap = element.attributeValue("parameterMap");
            if (StringUtils.isNotEmpty(parameterMap)) {
               throw new RuntimeException("暂时不支持 "+parameterMap);
            }
            String parameterType = element.attributeValue("parameterType");
            boolean singleParam = false;
            if (StringUtils.isNotEmpty(parameterType)) {  //参数

                //原值类型
                TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
                try{
                    typeAliasRegistry.resolveAlias(parameterType);
                    if (!StringUtils.equalsIgnoreCase(parameterType,"map")) {
                        singleParam = true; //去解析#{}
                    }
                }catch (Exception e){
                    //引用类型,如果抛出异常，则说明不是原始类型
                    EntityParam entityParam = SqlMapUtils.getEntityParamByclassType(parameterType);
                    SqlMapParam sqlMapParam = new SqlMapParam(entityParam.getAliasName(), entityParam.getEnName());
                    sqlMapMethod.addSqlMapParam(sqlMapParam);
                    sqlMapMethod.addImportCls(entityParam.getClsFullName());
                }
            }

            if(singleParam || StringUtils.isEmpty(parameterType) ){  //没有设置参数

                //
//                String textContent = element.getText();
                List<String> paramList = new ArrayList<>();
                //获取参数
                String pattern = "\\#\\{[^\\}]+\\}";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(text);
                Set<String> paramSet = new HashSet<>();
                while (m.find()) {
                    String group = m.group();
                    String paramName = group.substring(2, group.length() - 1);
                    paramSet.add(paramName);
                }
                List<Property> propertyList = currentEntityParam.getPropertyList();

                //foreach 特殊处理
                String foreachPattern = "\\<foreach[^\\<]+\\<\\/foreach\\>";
                Matcher matcher = Pattern.compile(foreachPattern).matcher(text);
                while (matcher.find()) {
                    String group = matcher.group();
                    inputStream = new ByteArrayInputStream(group.getBytes());
                    document = reader.read(inputStream);
                    Element rootElement = document.getRootElement();
                    String collection = rootElement.attributeValue("collection");
                    String item = rootElement.attributeValue("item");
                    for (Property property : propertyList) {
                        if (property.getEnName().equals(item)) {
                            String shortTypeName = SqlMapUtils.getShortTypeName(property.getDataType());
                            SqlMapParam sqlMapParam = new SqlMapParam("List<"+shortTypeName+">", collection);
                            sqlMapMethod.addSqlMapParam(sqlMapParam);
                            sqlMapMethod.addImportCls("java.util.List");
                            break;
                        }
                    }
                    paramSet.remove(item);
                }


                //set param
                for (String paramName : paramSet) {
                    for (Property property : propertyList) {
                        if (property.getEnName().equals(paramName)) {
                            SqlMapParam sqlMapParam = new SqlMapParam(SqlMapUtils.getShortTypeName(property.getDataType()), property.getEnName());
                            sqlMapMethod.addSqlMapParam(sqlMapParam);
                            break;
                        }
                    }
                }


            }
            //返回参数处理
            if (StringUtils.equals(tagName, "select")) {
                String resultType = element.attributeValue("resultType");
                String resultMap = element.attributeValue("resultMap");
                if (StringUtils.isNotEmpty(resultType)) {  //一般select 必须存在 resultType
                    TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

                    //原值类型
                    try{
                        Class<Object> objectClass = typeAliasRegistry.resolveAlias(resultType);
                        sqlMapMethod.setReturnType(objectClass.getTypeName());
                    }catch (Exception e){
                        //引用类型,如果抛出异常，则说明不是原始类型
                        EntityParam entityParam = SqlMapUtils.getEntityParamByclassType(parameterType);
                        sqlMapMethod.setReturnType(entityParam.getAliasName());
                        sqlMapMethod.addImportCls(entityParam.getClsFullName());
                    }
                } else if (StringUtils.isNotEmpty(resultMap)) {  //一般select 必须存在 resultMap
                    if (resultMap.equals("BaseResultMap")) {  //暂时这么处理，后需要反向找到resultMap type
                        sqlMapMethod.setReturnType(currentAlias);
                        sqlMapMethod.addImportCls(currentEntityParam.getClsFullName());
                    }
                } else {  //非查询
                    throw new RuntimeException("select must exist resultType or resultMap");
                }
                //处理返回object or list
                if (!sqlMapMethod.getName().startsWith("get") && !sqlMapMethod.getName().startsWith("find")) {  //list
                    sqlMapMethod.setReturnType("List<" + sqlMapMethod.getReturnType() + ">");
                    sqlMapMethod.addImportCls("java.util.List");
                }
            } else {
                sqlMapMethod.setReturnType(resultValue);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sqlMapMethod;
    }

    public static Map<String, String> anayseMyBatisConfig(String filePath) {
        Map<String, String> map = new HashMap<>();
        try {
            //创建解析器
            SAXReader reader = new SAXReader();
//            System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
            Document document = reader.read(new File(filePath));
            Element root = document.getRootElement();
            List<Node> typeAlias = root.selectNodes("typeAliases/typeAlias");

            for (Node node : typeAlias) {
                String alias = node.valueOf("@alias");
                String type = node.valueOf("@type");
                map.put(alias, type);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public static Map<String, String> anayseBeanPath(String parentPath, Map<String, String> aliasBeanMap) {
        Set<Map.Entry<String, String>> entries = aliasBeanMap.entrySet();
        Map<String, String> clssPathMap = new HashMap<>();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            String clsPath = parentPath + File.separator + value.replaceAll("\\.", File.separator) + ".java";
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


    public static void main(String[] args) throws IOException, DocumentException {

//        List<String> strings = IOUtils.readLines();
//        File configFile = new File("/Users/pro/workspace/darwin/darwin-dal/src/main/resources/mybatis-config.xml");
//        FileInputStream fileInputStream = new FileInputStream(new File("/Users/pro/workspace/darwin/darwin-dal/src/main/resources/mybatis-config.xml"));
//        Map<String, String> stringStringMap = anayseMyBatisConfig(fileInputStream);
//        System.out.println(stringStringMap);

        String text = FileUtils.readFileToString(new File("/Users/pro/aa.txt"));


        String pattern = "\\<foreach[^\\<]+\\<\\/foreach\\>";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        while (m.find()) {
            String group = m.group();
            System.out.println(""+group);
        }
//
//        SAXReader reader = new SAXReader();
//        InputStream inputStream = new ByteArrayInputStream(bytes);
//        Document document = reader.read(inputStream);
//        Element element = document.getRootElement();
//
//        System.out.printf(""+element);

    }
}
