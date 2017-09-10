package com.yuntao.zhushou.zplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.yuntao.zhushou.zplugin.ui.FieldSelForm;

/**
 * Created by shan on 2017/9/7.
 */
public class PopAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        FieldSelForm fieldSelForm = new FieldSelForm(e);
        String title = e.getPresentation().getText();
        fieldSelForm.start(title);
    }
}
