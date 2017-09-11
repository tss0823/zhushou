package com.yuntao.zhushou.zplugin;

import com.yuntao.zhushou.model.domain.codeBuild.Property;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shan on 2017/9/9.
 */
public class CodeSyncUtils {

    public static void newSync(String projectPath, String outFilePath) {
//        String projectPath = "/u01/workspace/fitness/";
//        String filePath = "/private/var/folders/7r/bl7hlc351dg1vfpdclb7r7bc0000gn/T/1504864546908.zip";
        //复制和替换文件到工作目录
//        String outFilePath = filePath.substring(0, filePath.length() - 4);
        try {
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
                    if (StringUtils.contains(newFilePath, "/model/domain/")) { //自身跳过 TODO
                        continue;
                    }
                    FileUtils.copyFile(leafFile, newFile);
                    System.out.println(newFilePath);

                }


            }
        } catch (Exception e) {
            throw new RuntimeException("newSync failed!", e);
        }

    }

    public static void updateSync(String projectPath, String outFilePath, EntityParam entityParam) {
//        String entityEnName = entityParam.getEnName();
        List<Property> propertyList = entityParam.getPropertyList();
        try {
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
                        String group = m.group();
//                        if(!StringUtils.contains(group,"public "+StringUtils.capitalize(entityEnName)+"Mapper")){  //没有匹配到
//                            continue;
//                        }
                        if (preStr != null) {
                            sb.append("\t");
                            sb.append(preStr);
                            sb.append("\n");
                            sb.append("\n");
                        }
                        preStr = group;
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

                } else {//

                    if (StringUtils.contains(newFilePath, "/model/domain/")) { //自身跳过 TODO
                        continue;
                    } else if (leafFile.getName().endsWith("Mapper.xml")) {
                        List<String> lines = FileUtils.readLines(leafFile);
//                        boolean resultFind = false;
                        boolean resultStart = false;
//                        boolean whereFind = false;
                        boolean whereStart = false;
                        boolean updateStart = false;
                        StringBuilder resultSb = new StringBuilder();
                        StringBuilder whereSb = new StringBuilder();
                        StringBuilder updateSb = new StringBuilder();
                        for (String line : lines) {
                            if (resultStart) {
                                for (Property property : propertyList) {
                                    if (StringUtils.contains(line, "<result column=\"" + property.getEnName() + "\"")) {
//                                        resultSb.append("\t");
                                        resultSb.append(line);
                                        resultSb.append("\n");
                                        break;
                                    }
                                }
                            }
                            if (whereStart) {
                                for (Property property : propertyList) {
                                    if (StringUtils.contains(line, "<if test=\"" + property.getEnName() + " !=")) {
//                                        whereSb.append("\t")t;
                                        whereSb.append(line);
                                        whereSb.append("\n");
                                        break;
                                    }
                                }
                            }
                            if (updateStart) {
                                for (Property property : propertyList) {
                                    if (StringUtils.contains(line, "<if test=\"" + property.getEnName() + " !=")) {
//                                        updateSb.append("\t\t");
                                        updateSb.append(line);
                                        updateSb.append("\n");
                                        break;
                                    }
                                }
                            }
                            if (StringUtils.contains(line, "id=\"BaseResultMap\">")) {
                                resultStart = true;
                            } else if (StringUtils.contains(line, "</resultMap>")) {
                                resultStart = false;
                            } else if (StringUtils.contains(line, "id=\"Base_Where\">")) {
                                whereStart = true;
                            } else if (StringUtils.contains(line, "</sql>")) {
                                whereStart = false;
                            } else if (StringUtils.contains(line, "<update id=\"updateById\"")) {
                                updateStart = true;
                            } else if (StringUtils.contains(line, "</update>")) {
                                updateStart = false;
                            }


                        }

                        List<String> readLines = FileUtils.readLines(newFile);
                        StringBuilder xmlSb = new StringBuilder();
                        boolean startResultMatch = false;
                        boolean whereMatch = false;
                        boolean updateMatch = false;
                        boolean columnListMatch = false;
                        boolean insertColumnMatch = false;
                        boolean insertCanMatch = false;
                        boolean insertValueMatch = false;

                        for (String line : readLines) {
                            if (startResultMatch && StringUtils.contains(line, "</resultMap>")) {
                                //add new content
                                xmlSb.append(resultSb.toString());
//                                startResultMatch = false;
                            } else if (whereMatch && StringUtils.contains(line, "</sql>")) {
                                //add new content
                                xmlSb.append(whereSb.toString());
//                                whereMatch = false;
                            } else if (updateMatch && StringUtils.contains(line, "</set>")) {
                                //add new content
                                xmlSb.append(updateSb.toString());
//                                whereMatch = false;
                            } else if (columnListMatch && StringUtils.contains(line, "</sql>")) {
                                //add new content
                                xmlSb.delete(xmlSb.length() - 1, xmlSb.length());  //先删除/n
                                for (Property property : propertyList) {
                                    xmlSb.append(",`"+property.getEnName()+"`");
                                }
                                xmlSb.append("\n");
                            }else if (insertColumnMatch && StringUtils.contains(line, ")")) {
                                //add new content
                                xmlSb.delete(xmlSb.length() - 1, xmlSb.length());  //先删除/n
                                for (Property property : propertyList) {
                                    xmlSb.append(",`"+property.getEnName()+"`");
                                }
                                xmlSb.append("\n");
                                insertColumnMatch = false;
                                insertCanMatch = true;
                            }else if (insertValueMatch && StringUtils.contains(line, ")")) {
                                //add new content
                                xmlSb.delete(xmlSb.length() - 1, xmlSb.length());  //先删除/n
                                for (Property property : propertyList) {
                                    xmlSb.append(",#{"+property.getEnName()+"}");
                                }
                                xmlSb.append("\n");
                                insertValueMatch = false;
                            }
                            xmlSb.append(line);
                            xmlSb.append("\n");

                            if (StringUtils.contains(line, "id=\"BaseResultMap\">")) {
                                startResultMatch = true;
                            } else if (StringUtils.contains(line, "</resultMap>")) {
                                startResultMatch = false;
                            } else if (StringUtils.contains(line, "id=\"Base_Column_List\">")) {
                                columnListMatch = true;
                            }else if (StringUtils.contains(line, "</sql>")) {
                                columnListMatch = false;
                            } else if (StringUtils.contains(line, "id=\"Base_Where\">")) {
                                whereMatch = true;
                            } else if (StringUtils.contains(line, "</sql>")) {
                                whereMatch = false;
                            } else if (StringUtils.contains(line, "<update id=\"updateById\"")) {
                                updateMatch = true;
                            } else if (StringUtils.contains(line, "</update>")) {
                                updateMatch = false;
                            }else if (StringUtils.contains(line, "insert into")) {
                                insertColumnMatch = true;
                            }else if (insertCanMatch && StringUtils.contains(line, "#")) {
                                insertValueMatch = true;
                                insertCanMatch = false;
                            }
                        }
                        xmlSb.delete(xmlSb.length() - 1, xmlSb.length());
                        FileUtils.write(newFile, xmlSb.toString());

                    }


//                    FileUtils.copyFile(leafFile, newFile);
//                    System.out.println(newFilePath);

                }


            }

        } catch (Exception e) {
            throw new RuntimeException("newSync failed!", e);
        }

    }

    public static void main(String[] args) {
        String projectPath = "/u01/workspace/fitness";
        String filePath = "/private/var/folders/7r/bl7hlc351dg1vfpdclb7r7bc0000gn/T/1505026440380";
        EntityParam entityParam = new EntityParam();
        List<Property> propertyList = new ArrayList<>();
        entityParam.setPropertyList(propertyList);
        Property property = new Property();
        property.setEnName("gmtModify");
        propertyList.add(property);
        property = new Property();
        property.setEnName("aa");
        propertyList.add(property);
        newSync(projectPath,filePath);
//        updateSync(projectPath,filePath,entityParam);

    }
}
