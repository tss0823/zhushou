package com.yuntao.zhushou.zplugin.sqlmap;

import com.yuntao.zhushou.model.domain.Entity;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import com.yuntao.zhushou.zplugin.sqlmap.bean.SqlMapMethod;
import com.yuntao.zhushou.zplugin.sqlmap.bean.SqlMapParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: shengshan.tang
 * @date: 2018/7/15 下午10:17
 */
public class SqlMapAnayse {

    private static Map<String,String> resultMap = new HashMap<>();
    static {
        resultMap.put("insert","int");
        resultMap.put("update","int");
        resultMap.put("delete","int");
        resultMap.put("select","List");
    }

    public static SqlMapMethod anayse(String text, Map<String,EntityParam> aliasEntityMap){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
//            String text = "<update id=\"deleteById\" parameterType=\"Long\">\n" +
//                    "        update <include refid=\"Base_Table_Name\"/> set isDelete = 1 where id = #{id}\n" +
//                    "    </update>";
            InputStream inputStream = new ByteArrayInputStream(text.getBytes());
            Document document = db.parse(inputStream);
            Element element = document.getDocumentElement();
            String tagName = element.getTagName();
            String resultValue = resultMap.get(tagName);
            if (StringUtils.isEmpty(resultValue)) {
                throw new RuntimeException("sqlmap 必须是 "+resultMap.keySet().toString());
            }

            SqlMapMethod sqlMapMethod = new SqlMapMethod();
            String id = element.getAttribute("id");
            sqlMapMethod.setName(id);

            //参数处理 TODO parameterMap
            String parameterType = element.getAttribute("parameterType");
            if (StringUtils.isNotEmpty(parameterType)) {  //参数
                String parameterTypeAlias = parameterType;
                //判断是否简称还是全名称
                int lastIndex = parameterType.lastIndexOf(".");
                if(lastIndex > 0){
                    //获取简称
                    parameterTypeAlias = parameterType.substring(lastIndex+1);
                }
                EntityParam entityParam = aliasEntityMap.get(parameterTypeAlias);
                if(entityParam == null){
                    throw new RuntimeException("暂不支持处理 "+parameterType);
                }
                SqlMapParam sqlMapParam = new SqlMapParam(parameterTypeAlias, StringUtils.uncapitalize(parameterTypeAlias));
                sqlMapMethod.addSqlMapParam(sqlMapParam);
                sqlMapMethod.addImportCls(entityParam.getClsFullName());

            }else{  //没有设置参数

            }
            //
            String textContent = element.getTextContent();
            List<String> paramList = new ArrayList<>();

            //获取参数
            String pattern = "\\#\\{[^\\}]+\\}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(textContent);
            while (m.find()) {
                String group = m.group();
                String paramName = group.substring(2,group.length()-1);
                if(paramList.contains(paramName)){
                    continue;
                }
                paramList.add(paramName);
            }

            //返回参数处理
            if (StringUtils.equals(tagName,"select")) {
                String resultType = element.getAttribute("resultType");
                String resultMap = element.getAttribute("resultMap");
                if(StringUtils.isNotEmpty(resultType)){  //一般select 必须存在 resultType
                    String resultTypeAlias = resultType;
                    //判断是否简称还是全名称
                    int lastIndex = resultType.lastIndexOf(".");
                    if(lastIndex > 0){
                        //获取简称
                        resultTypeAlias = resultType.substring(lastIndex+1);
                    }
                    EntityParam entityParam = aliasEntityMap.get(resultTypeAlias);
                    if(entityParam == null){
                        throw new RuntimeException("暂不支持处理 "+parameterType);
                    }
                    sqlMapMethod.setReturnType(resultTypeAlias);
                    sqlMapMethod.addImportCls(entityParam.getClsFullName());
                }else if(StringUtils.isNotEmpty(resultMap)){  //一般select 必须存在 resultMap

                }else{  //非查询
                   throw new RuntimeException("select must exist resultType or resultMap") ;
                }
            }else{
                sqlMapMethod.setReturnType(resultValue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String,String> anayseMyBatisConfig(String text){
        Map<String,String> map = new HashMap<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(text.getBytes());
            Document document = db.parse(inputStream);
            Element element = document.getDocumentElement();
            Node lastChild = element.getLastChild();
            NodeList childNodes = lastChild.getChildNodes();
            int length = childNodes.getLength();
            for (int i = 0; i < length; i++) {
                Node item = childNodes.item(i);
                Node alias = item.getNextSibling().getAttributes().getNamedItem("alias");
                Node type = item.getNextSibling().getAttributes().getNamedItem("type");
                map.put(alias.getNodeValue(),type.getNodeValue());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String,String> anayseBeanPath(String parentPath,Map<String,String>  aliasBeanMap){
        Set<Map.Entry<String, String>> entries = aliasBeanMap.entrySet();
        Map<String,String> clssPathMap = new HashMap<>();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            String clsPath = parentPath+"java"+File.separator+value.replaceAll("\\.",File.separator+".java");
            clssPathMap.put(key,clsPath);


        }
        return clssPathMap;
    }

    public static void main2(String[] args) {
        String pattern = "\\#\\{[^\\}]+\\}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher("abdf#{name}sfdfs=32#{age}");
        while (m.find()) {
            String group = m.group();
            String paramName = group.substring(2,group.length()-1);
            System.out.println(paramName);
        }
    }


    public static void main(String[] args) throws IOException {
        List<String> strings = IOUtils.readLines(new FileInputStream(new File("/Users/pro/workspace/darwin/darwin-dal/src/main/resources/mybatis-config.xml")));
        Map<String, String> stringStringMap = anayseMyBatisConfig(StringUtils.join(strings,""));
        System.out.println(stringStringMap);

    }
}
