package com.yuntao.zhushou;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shengshan.tang on 2015/5/28 10:49.
 * <p>
 * select COLUMN_NAME,DATA_TYPE, t.COLUMN_COMMENT from information_schema.`COLUMNS` t where TABLE_SCHEMA = 'hongbao' and TABLE_NAME = 'proxy'
 */
public class ModelGen {

    static Map<String, String> typeMap = new HashMap<String, String>();

    static {
        typeMap.put("varchar", "String");
        typeMap.put("int", "Integer");
        typeMap.put("bigint", "Long");
        typeMap.put("double", "Double");
        typeMap.put("datetime", "Date");
        typeMap.put("tinyint", "Boolean");
        typeMap.put("mediumblob", "byte []");
        typeMap.put("mediumtext", "String");
    }

    public static void main(String[] args) {
        try {
            List<String> list = FileUtils.readLines(new File("c:/test.txt"), "utf-8");
//            genBean(list);
//            genResultXml(list);
            columnGen(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void columnGen(List<String> list) throws IOException {
        StringBuilder columnSb = new StringBuilder();
        StringBuilder insertCSb = new StringBuilder();
        StringBuilder insertVSb = new StringBuilder();
        StringBuilder updateSb = new StringBuilder();
        StringBuilder setMethodSb = new StringBuilder();
        int i = 0;
        for (String str : list) {

            //select columns
            if (!str.equals("delStatus")) {
                columnSb.append("`");
                columnSb.append(str);
                columnSb.append("`");
                columnSb.append(",");
            }

            //insert columns
            if (!str.equals("id")) {
                insertCSb.append("`");
                insertCSb.append(str);
                insertCSb.append("`");
                insertCSb.append(",");
            }

            //insert values
            if (!str.equals("id")) {
                String propVal = "#{" + str + "}";
                if (str.equals("gmtCreate") || str.equals("gmtModify")) {
                    propVal = "now(6)";
                } else if (str.equals("delStatus")) {
                    propVal = "1";
                }
                insertVSb.append(propVal);
                insertVSb.append(",");
            }


            //update
            if (!str.equals("id") && !str.equals("gmtCreate") && !str.equals("delStatus")) {
                String propVal = "#{" + str + "}";
                if (str.equals("gmtModify")) {
                    continue;
                }
                updateSb.append("<if test=\"" + str + " != null and " + str + " !=''\">`" + str + "`=" + propVal + ",</if>\n");
            }

            //set method

            setMethodSb.append("shop.set" + StringUtils.capitalize(str) + "(\"abc" + (++i) + "\");\n");
        }
        String columnStr = columnSb.substring(0, columnSb.length() - 1);
        String insertCStr = insertCSb.substring(0, insertCSb.length() - 1);
        String insertVStr = insertVSb.substring(0, insertVSb.length() - 1);
        String updateStr = updateSb.toString();
        System.out.println(columnStr);
        System.out.println(insertCStr);
        System.out.println(insertVStr);
        System.out.println(updateStr);
//        System.out.println(setMethodSb.toString());
    }


    static void genBean(List<String> list) throws IOException {
        StringBuilder setMethodSb = new StringBuilder();
        for (String str : list) {
            String strs[] = str.split("\\s+");
            String prop = strs[0];
            String type = strs[1];
            String comment = strs[2];
            String javaType = typeMap.get(type);
//            System.out.println(prop+" "+type);
            //set method
            setMethodSb.append("/** " + comment + " */\n");
            setMethodSb.append("private " + javaType + " " + prop + ";\n\n");
        }
        System.out.println(setMethodSb.toString());
    }

    static void genResultXml(List<String> list) throws IOException {
        StringBuilder resultMapSb = new StringBuilder();
        for (String str : list) {
            String strs[] = str.split("\\s+");
            String prop = strs[0];
            String type = strs[1].toUpperCase();
            if (type.equals("INT")) {
                type = "INTEGER";
            } else if (type.equals("DATETIME")) {
                type = "TIMESTAMP";
            }
            resultMapSb.append("<");
            if (prop.equals("id")) {
                resultMapSb.append("id");
            } else {
                resultMapSb.append("result");

            }
            resultMapSb.append(" column=\"" + prop + "\" property=\"" + prop + "\" jdbcType=\"" + type + "\" />\n");
        }
        System.out.println(resultMapSb.toString());
    }


}
