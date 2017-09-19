package com.yuntao.zhushou.zplugin.ui;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * Created by shan on 2017/9/19.
 */
public class MyFrame extends JFrame {

    private volatile boolean canClose = true;

    @Override
    protected void processWindowEvent(WindowEvent windowEvent) {
        if(canClose){
            super.processWindowEvent(windowEvent);
        }

    }
}
