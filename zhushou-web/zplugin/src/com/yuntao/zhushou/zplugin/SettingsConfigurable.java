package com.yuntao.zhushou.zplugin;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.zplugin.ui.SettingsForm;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by shan on 2017/9/7.
 */
public class SettingsConfigurable implements SearchableConfigurable {

    private SettingsForm settingsForm = new SettingsForm();

    @NotNull
    @Override
    public String getId() {
        return "zhushou";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return getId();
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return getId();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        User accountInfo = ZpluginUtils.getAccountInfo();
        settingsForm.getTxtAccountNo().setText(accountInfo.getAccountNo());
        settingsForm.getTxtPwd().setText(accountInfo.getPwd());
        String logPath = ZpluginUtils.getLogPath();
        settingsForm.getTxtLogPath().setText(logPath);
        String testBranch = ZpluginUtils.getTestBranch();
        settingsForm.getTxtTestBranch().setText(testBranch);
        String appPorts = ZpluginUtils.getValue(ZpluginConstant.appPorts);
        settingsForm.getTxtAppPorts().setText(appPorts);
        return settingsForm.$$$getRootComponent$$$();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        ZpluginUtils.setAccountInfo(settingsForm.getTxtAccountNo().getText(),settingsForm.getTxtPwd().getText());
        ZpluginUtils.setTestBranch(settingsForm.getTxtTestBranch().getText());
        ZpluginUtils.setLogPath(settingsForm.getTxtLogPath().getText());
        ZpluginUtils.setValue(ZpluginConstant.appPorts,settingsForm.getTxtAppPorts().getText());
    }

    @Override
    public void reset() {
        User accountInfo = ZpluginUtils.getAccountInfo();
        settingsForm.getTxtAccountNo().setText(accountInfo.getAccountNo());
        settingsForm.getTxtPwd().setText(accountInfo.getPwd());
        String logPath = ZpluginUtils.getLogPath();
        settingsForm.getTxtLogPath().setText(logPath);
        String testBranch = ZpluginUtils.getTestBranch();
        settingsForm.getTxtTestBranch().setText(testBranch);
        String appPorts = ZpluginUtils.getValue(ZpluginConstant.appPorts);
        settingsForm.getTxtAppPorts().setText(appPorts);
    }
}
