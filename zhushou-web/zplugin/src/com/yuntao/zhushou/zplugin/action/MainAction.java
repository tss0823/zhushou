package com.yuntao.zhushou.zplugin.action;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import com.yuntao.zhushou.common.utils.ExceptionUtils;
import com.yuntao.zhushou.zplugin.WsUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.swing.*;

/**
 * Created by shan on 2017/9/7.
 */
public class MainAction extends DumbAwareAction {

    protected final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    protected final static Logger log = org.slf4j.LoggerFactory.getLogger(MainAction.class);

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        try {
            if (e.getPresentation().getText().equals("部署测试环境")) {
                //启动ws
                if (WsUtils.isClose()) {
                   WsUtils.openWsConnect();
                }
                ActionManager inst = ActionManager.getInstance();
                AnAction other = inst.getAction("OpenFixFileConsoleAction");  // another plugin's action
//                if(other.getTemplatePresentation().isVisible()){
//
//                    System.out.printf("可以见");
//                }else {
//                    System.out.printf("不可见");
//                }

                //执行发布
                com.yuntao.zhushou.zplugin.ActionManager actionManager = new com.yuntao.zhushou.zplugin.ActionManager();
                other.actionPerformed(e);
                actionManager.deployTest();

                //fire GrepConsole action

            }
        } catch (Exception ex) {
            log.error("mainAction execute failed!",ex);
            String message = ex.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = ExceptionUtils.getPrintStackTrace(ex);
            }
            JOptionPane.showMessageDialog(e.getData(DataKeys.EDITOR).getComponent(), message, "错误", JOptionPane.ERROR_MESSAGE);
        }
    }



}
