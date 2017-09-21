package com.yuntao.zhushou.zplugin.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Created by shan on 2017/9/19.
 */
public class MyFrame extends JFrame {

    private volatile boolean canClose = true;

    public MyFrame() throws HeadlessException {
        this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    }

    @Override
    protected void processWindowEvent(WindowEvent windowEvent) {
        if(canClose){
            super.processWindowEvent(windowEvent);
        }

    }

    public void setCanClose(boolean canClose) {
        this.canClose = canClose;
    }

    public boolean isCanClose() {
        return canClose;
    }
}
