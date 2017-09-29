package com.yuntao.zhushou.zplugin.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.ExceptionUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.zplugin.ZhushouRpcUtils;
import com.yuntao.zhushou.zplugin.ZpluginConstant;
import com.yuntao.zhushou.zplugin.ZpluginUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

/**
 * Created by shan on 2017/9/25.
 */
public class HotAppSelForm {

    protected final static Logger bisLog = LoggerFactory.getLogger("bis");

    protected final static Logger log = LoggerFactory.getLogger(FieldSelForm.class);

    private JPanel jplMain;
    private JComboBox cbxAppName;
    private JButton btnCancel;
    private JButton btnOk;

    private MyFrame frame = new MyFrame();

    public HotAppSelForm() {
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
//                String url = "http://localhost:"+
                String appName = cbxAppName.getSelectedItem().toString();
                try {
                    ZhushouRpcUtils.hotReload(appName);
                    ZpluginUtils.setValue(ZpluginConstant.currentAppName, appName);
                    JOptionPane.showMessageDialog(jplMain, "部署完成");
                    frame.setVisible(false);
                } catch (Exception ex) {
                    log.error("hot reload execute failed!", ex);
                    String message = ex.getMessage();
                    if (StringUtils.isEmpty(message)) {
                        message = ExceptionUtils.getPrintStackTrace(ex);
                    }
                    JOptionPane.showMessageDialog(jplMain, message, "错误", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //close hide
                if (frame.isCanClose()) {
                    frame.setVisible(false);
                }
            }
        });
    }

    public void start(String title) {
        frame.setTitle(title);
        frame.setContentPane(this.jplMain);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        frame.setLocationByPlatform(true);
        frame.pack();

        frame.setLocationRelativeTo(null);
//        frame.setSize(270, 450);
        frame.setSize(220, 135);
//        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

//        Vector<Object> objects = new Vector<>();
        DefaultComboBoxModel cbxModel = new DefaultComboBoxModel();

        try {
            //获取应用列表
            String value = ZpluginUtils.getValue(ZpluginConstant.appPorts);
            if (StringUtils.isEmpty(value)) {
                throw new BizException("请先设置好app端口号");
            }
            Map<String, Integer> appPortMap = JsonUtils.json2Object(value, Map.class);
            Set<Map.Entry<String, Integer>> entries = appPortMap.entrySet();
            String currentAppName = ZpluginUtils.getValue(ZpluginConstant.currentAppName);
            for (Map.Entry<String, Integer> entry : entries) {
                cbxModel.addElement(entry.getKey());
            }
            this.cbxAppName.setModel(cbxModel);
            if (StringUtils.isNotEmpty(currentAppName)) {
                this.cbxAppName.setSelectedItem(currentAppName);
            }

        } catch (Exception ex) {
            log.error("start execute failed!", ex);
            String message = ex.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = ExceptionUtils.getPrintStackTrace(ex);
            }
            JOptionPane.showMessageDialog(jplMain, message, "错误", JOptionPane.ERROR_MESSAGE);
        }

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jplMain = new JPanel();
        jplMain.setLayout(new GridLayoutManager(2, 2, new Insets(10, 6, 10, 5), -1, -1));
        jplMain.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "热部署应用选择"));
        final JLabel label1 = new JLabel();
        label1.setText("应用");
        jplMain.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbxAppName = new JComboBox();
        jplMain.add(cbxAppName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCancel = new JButton();
        btnCancel.setText("取消");
        jplMain.add(btnCancel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnOk = new JButton();
        btnOk.setText("确认");
        jplMain.add(btnOk, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jplMain;
    }
}