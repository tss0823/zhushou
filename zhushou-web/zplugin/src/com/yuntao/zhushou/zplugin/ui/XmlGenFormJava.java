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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //生成java method code
                final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
                final Project project = event.getProject();
                //Access document, caret, and selection
                final Document document = editor.getDocument();
                final SelectionModel selectionModel = editor.getSelectionModel();
                String text = selectionModel.getSelectedText();
                if (StringUtils.isBlank(text)) {
                    throw new RuntimeException("请选择sqlMap块再操作");
                }



                PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
                PsiDirectory parent = psiFile.getParent();
                String parentPath = parent.getVirtualFile().getParent().getPath();
                VirtualFile mybatisConfigFile = LocalFileSystem.getInstance().findFileByPath(parentPath + File.separator + "mybatis-config.xml");

                try {
                    InputStream inputStream = mybatisConfigFile.getInputStream();
                    List<String> strings = IOUtils.readLines(inputStream);
                    Map<String, String> stringStringMap = SqlMapAnayse.anayseMyBatisConfig(StringUtils.join(strings));
                    String domainPath = parentPath.substring(0,parentPath.lastIndexOf(File.separator))+"java";
                    Map<String, String> clsPathMap = SqlMapAnayse.anayseBeanPath(domainPath, stringStringMap);
                    Set<Map.Entry<String, String>> entries = clsPathMap.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(entry.getValue());
                        PsiFile clsPsiFile = PsiManager.getInstance(project).findFile(virtualFile);
                        EntityParam entityParam = AnalyseModelUtils.analyseProperty(clsPsiFile);

                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


//                SqlMapMethod sqlMapMethod = SqlMapAnayse.anayse(text);


//                JOptionPane.showMessageDialog(mainJpanel, text);
//                System.out.println(text);

            }
        });

        chkDao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XmlGenFormJava.this.chkAction(e);
            }
        });
        chkManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XmlGenFormJava.this.chkAction(e);
            }
        });

        chkService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XmlGenFormJava.this.chkAction(e);
            }
        });

        chkClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XmlGenFormJava.this.chkAction(e);
            }
        });

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
