package com.yuntao.zhushou.zplugin.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.ExceptionUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import com.yuntao.zhushou.zplugin.AnalyseModelUtils;
import com.yuntao.zhushou.zplugin.ZpluginConstant;
import com.yuntao.zhushou.zplugin.ZpluginUtils;
import com.yuntao.zhushou.zplugin.sqlmap.SqlMapAnayse;
import com.yuntao.zhushou.zplugin.sqlmap.SqlMapContextMgr;
import com.yuntao.zhushou.zplugin.sqlmap.bean.SqlMapMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author: shengshan.tang
 * @date: 2018/7/15 下午4:17
 */
public class XmlGenFormJava {
    private JPanel mainJpanel;
    private JButton btnOk;
    private JCheckBox chkMapper;
    private JCheckBox chkService;
    private JCheckBox chkManager;
    private JCheckBox chkDao;
    private JCheckBox chkClient;


    private MyFrame frame = new MyFrame();

    List<JCheckBox> chkList = new ArrayList<>();

    {
        chkList.add(chkMapper);
        chkList.add(chkDao);
        chkList.add(chkManager);
        chkList.add(chkService);
        chkList.add(chkClient);
    }


    public XmlGenFormJava(AnActionEvent event) {
        btnOk.addActionListener(e -> {
            //生成java method code
            final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
            final Project project = event.getProject();
            SqlMapContextMgr.project = project;
            //TODO
            //Access document, caret, and selection
//                final Document document = editor.getDocument();
            final SelectionModel selectionModel = editor.getSelectionModel();
            String text = selectionModel.getSelectedText();
            if (StringUtils.isBlank(text)) {
                throw new RuntimeException("请选择sqlMap块再操作");
            }

            PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
            PsiDirectory parent = psiFile.getParent();
            String parentPath = parent.getVirtualFile().getParent().getPath();
//                VirtualFile mybatisConfigFile = LocalFileSystem.getInstance().findFileByPath();
            String configFilePath = parentPath + File.separator + "mybatis-config.xml";
            Map<String, String> stringStringMap = SqlMapAnayse.anayseMyBatisConfig(configFilePath);
            SqlMapContextMgr.entityNameAliasMap = stringStringMap;


            //获取当前实体alias
            String mapperFileName = psiFile.getName();
            int index = mapperFileName.lastIndexOf("Mapper.xml");
            String prefix = mapperFileName.substring(0,index);

            SqlMapMethod sqlMapMethod = SqlMapAnayse.anayse(text,prefix);
            JOptionPane.showMessageDialog(mainJpanel, sqlMapMethod);


//                System.out.println(text);

        });

        chkDao.addActionListener(e -> XmlGenFormJava.this.chkAction(e));
        chkManager.addActionListener(e -> XmlGenFormJava.this.chkAction(e));

        chkService.addActionListener(e -> XmlGenFormJava.this.chkAction(e));

        chkClient.addActionListener(e -> XmlGenFormJava.this.chkAction(e));

    }

    private void chkAction(ActionEvent e) {
        JCheckBox source = (JCheckBox) e.getSource();
        boolean selected = source.getModel().isSelected();
//        JOptionPane.showMessageDialog(mainJpanel, "selected="+selected);
        boolean searched = false;
        for (JCheckBox jCheckBox : chkList) {
            if (jCheckBox.equals(source)) {
                searched = true;
                continue;
            }
            if (!searched && selected) {//下级全部选中
                jCheckBox.getModel().setSelected(true);
            } else if (searched && !selected) {//上级全部取消
                jCheckBox.getModel().setSelected(false);
            }
        }
    }

    public void start(String title) {

        frame.setTitle(title);
        frame.setContentPane(this.mainJpanel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        frame.setLocationByPlatform(true);
        frame.pack();

        frame.setLocationRelativeTo(null);
//        frame.setSize(270, 450);
        frame.setSize(220, 260);
//        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

//        Vector<Object> objects = new Vector<>();
//        DefaultComboBoxModel cbxModel = new DefaultComboBoxModel();


    }

}
