/**
 *
 */
package com.yuntao.zhushou.model.vo.codeBuild;

import com.yuntao.zhushou.model.domain.Project;
import com.yuntao.zhushou.model.domain.Template;
import com.yuntao.zhushou.model.domain.codeBuild.DbConfigure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangss
 * @2013年8月31日 @下午4:58:27
 */
public class ProjectBo extends Project {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private DbConfigure dbConfigure;

    private Template template;

    private String userName;

    private List<EntityBo> entityBoList = new ArrayList<EntityBo>();


    public DbConfigure getDbConfigure() {
        return dbConfigure;
    }

    public void setDbConfigure(DbConfigure dbConfigure) {
        this.dbConfigure = dbConfigure;
    }

    public List<EntityBo> getEntityBoList() {
        return entityBoList;
    }

    public void setEntityBoList(List<EntityBo> entityBoList) {
        this.entityBoList = entityBoList;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
