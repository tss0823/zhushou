package com.yuntao.zhushou.zplugin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Alexey.Chursin
 * Date: Aug 13, 2010
 * Time: 3:50:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class Zproject implements ProjectComponent {
    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");
    protected final static Logger log = org.slf4j.LoggerFactory.getLogger(ActionManager.class);

    public Zproject(Project project) {
        String name = project.getBaseDir().getName();
        bisLog.info("projectName="+name);
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return "zProject";
    }

    public void projectOpened() {
        // called when project is opened
        ZApplicationService applicationService = ServiceManager.getService(ZApplicationService.class);

        String componentName = getComponentName();
        bisLog.info("componentName="+componentName);

    }


    public void projectClosed() {
        // called when project is being closed
    }
}