package com.yuntao.zhushou.zplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.yuntao.zhushou.zplugin.ui.XmlGenFormJava;

/**
 * Created by shan on 2017/9/7.
 */
public class PopXmlAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        //Get all the required data from data keys
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getProject();
        //Access document, caret, and selection
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();
        String text = selectionModel.getSelectedText();
        System.out.println(text);


        XmlGenFormJava xmlGenJava = new XmlGenFormJava(e);
        String title = e.getPresentation().getText();
        xmlGenJava.start(title);

    }
}
