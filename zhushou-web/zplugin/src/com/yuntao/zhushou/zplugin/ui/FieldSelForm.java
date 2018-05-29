package com.yuntao.zhushou.zplugin.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.ExceptionUtils;
import com.yuntao.zhushou.model.domain.Property;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import com.yuntao.zhushou.zplugin.ActionManager;
import com.yuntao.zhushou.zplugin.AnalyseModelUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

//import java.awt.event.ActionEvent;

//import java.awt.event.ActionEvent;

//import java.awt.event.import com.intellij.openapi.actionSystem.AnActionEvent;ActionEvent;

//import java.awt.event.ActionEvent;


/**
 * Created by shan on 2017/9/7.
 */
public class FieldSelForm {
    protected final static Logger bisLog = LoggerFactory.getLogger("bis");

    protected final static Logger log = LoggerFactory.getLogger(FieldSelForm.class);

    private MyFrame frame = new MyFrame();
    private JPanel mainPanel;
    private JList listMethod;
    private JButton btnExecute;
    private JButton btnCancel;
    private JPanel jplButton;
    private JPanel jplMsg;
    private JTextPane txtPalProcess;

    private AnActionEvent event;

    private EntityParam entityParam;


    public FieldSelForm(final AnActionEvent event) {
        this.event = event;
        final String actionText = event.getPresentation().getText();
        final String projectFilePath = event.getProject().getBasePath();
        btnExecute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                AnalyseModelUtils.analyseProperty(event);
//                ListSelectionModel selectionModel = listMethod.getSelectionModel();
                frame.setContentPane(mainPanel);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int result = JOptionPane.showConfirmDialog(frame, "您确认要" + actionText + "？", "提示", JOptionPane.YES_NO_OPTION);
                        if (result == 1) {  //1 取消，0 确认
                            return;
                        }
                        try {
                            if (!actionText.equals("删除实体")) {
                                if (actionText.equals("新建实体")) {
                                    int seleSize = listMethod.getLastVisibleIndex() + 1;

                                    int selectArray[] = new int[seleSize];
                                    for (int i = 0; i < seleSize; i++) {
                                        selectArray[i] = i;
                                    }
                                    listMethod.setSelectedIndices(selectArray);
                                }
                                List<Property> selectedValuesList = listMethod.getSelectedValuesList();
                                if (CollectionUtils.isEmpty(selectedValuesList)) {
                                    throw new BizException("请选择要您要操作的项");
                                }
                                entityParam.setPropertyList(selectedValuesList);
                            }

                            frame.setCanClose(false);
                            btnExecute.setEnabled(false);
                            txtPalProcess.setVisible(true);
                            txtPalProcess.setText("执行中，请稍后...");

                            ActionManager actionManager = new ActionManager();
                            btnExecute.setEnabled(false);
                            txtPalProcess.setVisible(true);
                            txtPalProcess.setText("执行中，请稍后...");
//                            Thread.sleep(5000);
                            if (actionText.equals("新建实体")) {
                                actionManager.newEntity(projectFilePath, entityParam);
                            } else if (actionText.equals("添加属性")) {
                                actionManager.addProperty(projectFilePath, entityParam);

                            } else if (actionText.equals("删除属性")) {
                                actionManager.delProperty(projectFilePath, entityParam);

                            } else if (actionText.equals("删除实体")) {
                                actionManager.delEntity(projectFilePath, entityParam);

                            } else {
                                throw new BizException("操作项错误");
                            }
                            txtPalProcess.setText("执行完成!");
                        } catch (Exception ex) {
                            log.error("actionPerformed execute failed!", ex);
                            String message = ex.getMessage();
                            if (StringUtils.isEmpty(message)) {
                                message = ExceptionUtils.getPrintStackTrace(ex);
                            }
                            JOptionPane.showMessageDialog(frame, message, "错误", JOptionPane.ERROR_MESSAGE);
                            btnExecute.setEnabled(true);
                            txtPalProcess.setText("执行出错了...");
                        }
                        frame.setCanClose(true);

                    }
                }).start();


            }

        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //close hide
                if (frame.isCanClose()) {
                    frame.setVisible(false);
                }

            }
        });
    }

    public void start(String title) {
        frame.setTitle(title);
        frame.setContentPane(this.mainPanel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        frame.setLocationByPlatform(true);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setSize(270, 450);
//        frame.setSize(600,500);
//        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

//        Vector<Object> objects = new Vector<>();
        DefaultListModel listModel = new DefaultListModel();

        try {
            entityParam = AnalyseModelUtils.analyseProperty(this.event);
            if (entityParam != null && entityParam.getPropertyList() != null) {
                for (Property property : entityParam.getPropertyList()) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(property.getEnName());
//                sb.append(":");
//                sb.append(property.getCnName());
//                sb.append(" [" + property.getDataType() + ":" + (StringUtils.isNotEmpty(property.getLength()) ? property.getLength() : "") + "]");
//                objects.add(sb.toString());
                    listModel.addElement(property);
                }
            }
//        listMethod.setListData(objects);
            listMethod.setModel(listModel);

        } catch (Exception ex) {
            log.error("start execute failed!", ex);
            String message = ex.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = ExceptionUtils.getPrintStackTrace(ex);
            }
            JOptionPane.showMessageDialog(mainPanel, message, "错误", JOptionPane.ERROR_MESSAGE);
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "属性选择", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listMethod = new JList();
        listMethod.setAlignmentX(1.0f);
        listMethod.setAlignmentY(1.0f);
        listMethod.setFont(UIManager.getFont("ColorChooser.font"));
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("111");
        defaultListModel1.addElement("2222");
        listMethod.setModel(defaultListModel1);
        listMethod.setVisibleRowCount(15);
        scrollPane1.setViewportView(listMethod);
        jplButton = new JPanel();
        jplButton.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(jplButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnExecute = new JButton();
        btnExecute.setText("执行");
        jplButton.add(btnExecute, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCancel = new JButton();
        btnCancel.setText("取消");
        jplButton.add(btnCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jplMsg = new JPanel();
        jplMsg.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(jplMsg, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txtPalProcess = new JTextPane();
        txtPalProcess.setForeground(new Color(-4504294));
        txtPalProcess.setText("");
        txtPalProcess.setVisible(false);
        jplMsg.add(txtPalProcess, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
