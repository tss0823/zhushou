package com.yuntao.zhushou.zplugin;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.model.domain.Property;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

//import java.awt.event.ActionEvent;

//import java.awt.event.ActionEvent;


/**
 * Created by shan on 2017/9/7.
 */
public class AnalyseModelUtils {
    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");
    protected final static Logger log = org.slf4j.LoggerFactory.getLogger(AnalyseModelUtils.class);

    public AnalyseModelUtils() {
    }

    public static EntityParam analysePropertyForXmlGen(PsiFile psiFile) {
        EntityParam entityParam = new EntityParam();
        PsiClass psiClass = PsiTreeUtil.findChildOfAnyType(psiFile, PsiClass.class);
        if (psiClass == null) {
            throw new BizException("请选择实体类去生成");
        }

        PsiAnnotation[] clsAnnotions = psiClass.getModifierList().getAnnotations();
        String clsEnName = psiClass.getName();
        String entityEnName = StringUtils.uncapitalize(clsEnName);
        entityParam.setEnName(entityEnName);

        if (clsAnnotions != null && clsAnnotions.length > 0) {
            PsiNameValuePair[] clsAttributes = clsAnnotions[0].getParameterList().getAttributes();
            for (PsiNameValuePair clsAttribute : clsAttributes) {
                String value = clsAttribute.getValue().getText();
                value = value.substring(1, value.length() - 1);
                entityParam.setCnName(value);
            }
        }

        PsiField[] allFields = psiClass.getAllFields();
        List<Property> propertyList = new ArrayList<>();
        entityParam.setPropertyList(propertyList);
        for (PsiField field : allFields) {
            String enName = field.getName();
            String dataType = field.getType().getCanonicalText();
            Property property = new Property();
            property.setEnName(enName);
            property.setDataType(dataType);
            property.setIsNull(true);
            if (enName.equals("id")) {
                property.setPrimaryKey(true);
                property.setIsNull(false);
            } else {
                property.setPrimaryKey(false);
            }
            PsiAnnotation[] annotations = field.getModifierList().getAnnotations();
            if (annotations != null && annotations.length > 0) {
                PsiNameValuePair[] attributes = annotations[0].getParameterList().getAttributes();
                for (PsiNameValuePair attribute : attributes) {
                    try {
                        String name = attribute.getName();
                        String text = attribute.getValue().getText();
                        if (name.equals("value")) {
                            text = text.substring(1, text.length() - 1);
                            property.setCnName(text);
                        } else if (name.equals("required") && text.equals("true")) {
                            property.setIsNull(false);
                        } else if (name.equals("maxLength")) {
                            property.setLength(text);
                        } else if (name.equals("defaultValue")) {
                            text = text.substring(1, text.length() - 1);
                            property.setDefaultValue(text);
                        }

                    } catch (Exception e) {
                        break;
                    }
                }
            }
            propertyList.add(property);
        }
        return entityParam;

    }

    public static EntityParam analyseProperty(PsiFile psiFile) {
        EntityParam entityParam = new EntityParam();
        PsiClass psiClass = PsiTreeUtil.findChildOfAnyType(psiFile, PsiClass.class);
        if (psiClass == null) {
            throw new BizException("请选择实体类去生成");
        }

        PsiAnnotation[] clsAnnotions = psiClass.getModifierList().getAnnotations();
        String clsEnName = psiClass.getName();
        String entityEnName = StringUtils.uncapitalize(clsEnName);
        entityParam.setEnName(entityEnName);

        if (clsAnnotions == null || clsAnnotions.length == 0) {
            throw new RuntimeException("实体注解不能为空");
        }
        PsiNameValuePair[] clsAttributes = clsAnnotions[0].getParameterList().getAttributes();
        if (clsAttributes == null || clsAttributes.length == 0) {
            throw new RuntimeException("实体注解参数不能为空");
        }
        for (PsiNameValuePair clsAttribute : clsAttributes) {
            String name = clsAttribute.getName();
            String value = clsAttribute.getValue().getText();
            value = value.substring(1, value.length() - 1);
            if (StringUtils.isBlank(name) || StringUtils.equalsIgnoreCase(name,"value")) {
                entityParam.setCnName(value);
            }else if(StringUtils.equalsIgnoreCase(name,"tableName")){
                entityParam.setTableName(value);
            }
        }
        if (StringUtils.isEmpty(entityParam.getTableName())) {
            entityParam.setTableName(StringUtils.uncapitalize(entityParam.getEnName()));
        }

        PsiField[] allFields = psiClass.getAllFields();
        List<Property> propertyList = new ArrayList<>();
        entityParam.setPropertyList(propertyList);
        for (PsiField field : allFields) {
            String enName = field.getName();
            String dataType = field.getType().getCanonicalText();
            PsiAnnotation[] annotations = field.getModifierList().getAnnotations();
            if (annotations == null || annotations.length == 0) {
                continue;
            }
            PsiNameValuePair[] attributes = annotations[0].getParameterList().getAttributes();
            if (attributes == null || attributes.length == 0) {
                continue;
            }
            Property property = new Property();
            property.setEnName(enName);
            property.setDataType(dataType);
            property.setIsNull(true);
            if (enName.equals("id")) {
                property.setPrimaryKey(true);
                property.setIsNull(false);
            } else {
                property.setPrimaryKey(false);
            }
            boolean state = true;
            for (PsiNameValuePair attribute : attributes) {
                try {
                    String name = attribute.getName();
                    String text = attribute.getValue().getText();
                    if (name.equals("value")) {
                        text = text.substring(1, text.length() - 1);
                        property.setCnName(text);
                    } else if (name.equals("required") && text.equals("true")) {
                        property.setIsNull(false);
                    } else if (name.equals("maxLength")) {
                        property.setLength(text);
                    } else if (name.equals("defaultValue")) {
                        text = text.substring(1, text.length() - 1);
                        property.setDefaultValue(text);
                    }

                } catch (Exception e) {
                    state = false;
                    break;
                }
            }
            if (state) {
                propertyList.add(property);
            }
        }
        return entityParam;
    }

    public static EntityParam analyseProperty(AnActionEvent event) {
//        event.getPresentation().setEnabledAndVisible(true);
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
//        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (psiFile != null) {
//            int offset = editor.getCaretModel().getOffset();
//            PsiElement elementAt = psiFile.findElementAt(offset);
            return analyseProperty(psiFile);

        }
        return null;
    }
}
