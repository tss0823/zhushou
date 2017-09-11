package com.yuntao.zhushou.zplugin;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXFrame;

/**
 * Created by shan on 2017/9/10.
 */
public class MainTest {

    public static void main(String[] args) {
        JXFrame frame = new JXFrame("test", true);
        JXBusyLabel label = new JXBusyLabel();
        frame.add(label);
        frame.setSize(200, 300);
        frame.setLocationByPlatform(true);
        label.setBusy(true);
        frame.setVisible(true);
    }
}
