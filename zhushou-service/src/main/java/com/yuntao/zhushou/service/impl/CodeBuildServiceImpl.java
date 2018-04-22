package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DbUtils;
import com.yuntao.zhushou.model.domain.*;
import com.yuntao.zhushou.model.domain.codeBuild.DbConfigure;
import com.yuntao.zhushou.model.query.PropertyQuery;
import com.yuntao.zhushou.model.vo.codeBuild.EntityBo;
import com.yuntao.zhushou.model.vo.codeBuild.ProjectBo;
import com.yuntao.zhushou.model.vo.codeBuild.PropertyBo;
import com.yuntao.zhushou.service.inter.*;
import com.yuntao.zhushou.service.support.codeBuild.ApplicationBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shan on 2017/8/29.
 */
@Service
public class CodeBuildServiceImpl extends AbstService implements CodeBuildService {

    @Value("${codeBuild.url}")
    private String codeBuildUrl;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ApplicationBuilder applicationBuilder;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private BuildRecordService buildRecordService;

    @Autowired
    private AttachmentService attachmentService;





    @Transactional
    @Override
    public int entityCopy(Long id) {
        Entity entity = entityService.findById(id);
        //save new entity
        entity.setEnName(entity.getEnName()+"_copy");
        entityService.insert(entity);
        PropertyQuery propertyQuery = new PropertyQuery();
        propertyQuery.setEntityId(id);
        List<Property> propertyList = propertyService.selectList(propertyQuery);
        for (Property property : propertyList) {
            property.setEntityId(entity.getId());
            propertyService.insert(property);
        }
        return 1;
    }

    @Override
    public String buildSql(User user,Long projectId, List<Long> entityIds) {
        ProjectBo projectPo = this.getProjectPo(user, projectId, entityIds);
        List<EntityBo> entityBoList = projectPo.getEntityBoList();
        StringBuilder sb = new StringBuilder();
        for (EntityBo entityBo : entityBoList) {
            String str = applicationBuilder.buildTableSql(entityBo,entityBo.getPropList());
            sb.append(str);
        }
        return sb.toString();
    }

    private ProjectBo getProjectPo(User user,Long projectId,List<Long> entityIds){
        //构建projectBo
        Project project = projectService.findById(projectId);
        ProjectBo projectBo = BeanUtils.beanCopy(project,ProjectBo.class);
//        projectBo.setBuildSingle(isSingle);
        projectBo.setUserName(user.getNickName());

        DbConfigure dbConfigure = this.getDbConfigure(projectId);
        projectBo.setDbConfigure(dbConfigure);

        Template template = templateService.findById(project.getTemplateId());
        projectBo.setTemplate(template);

        List<EntityBo> entityBoList = new ArrayList<>();
        projectBo.setEntityBoList(entityBoList);
        for (Long entityId : entityIds) {
            Entity entity = entityService.findById(entityId);
            EntityBo entityBo = BeanUtils.beanCopy(entity,EntityBo.class);
            String tableName = DbUtils.javaToTableName(entity.getEnName());
            entityBo.setTableName(tableName);
            String upperEntityEnName = StringUtils.capitalize(entity.getEnName());
            entityBo.setUpperEntityEnName(upperEntityEnName);
            entityBoList.add(entityBo);

            //propertyBo
            PropertyQuery propertyQuery = new PropertyQuery();
            propertyQuery.setEntityId(entityId);
            List<Property> propertyList = propertyService.selectList(propertyQuery);
            List<PropertyBo> propertyBoList = new ArrayList<>();
            entityBo.setPropList(propertyBoList);
            for (Property property : propertyList) {
                PropertyBo propertyBo = BeanUtils.beanCopy(property,PropertyBo.class);
                String columnName = DbUtils.javaToTableName(property.getEnName());
                String upperPropertyEnName = StringUtils.capitalize(property.getEnName());
                propertyBo.setUpperEnName(upperPropertyEnName);
                propertyBo.setColumnName(columnName);
                propertyBoList.add(propertyBo);
            }
        }

        return projectBo;
    }

    @Transactional
    @Override
    public Long buildApp(boolean isSingle,User user, Long projectId, List<Long> entityIds) {
        ProjectBo projectBo = this.getProjectPo(user, projectId, entityIds);

        String genZipPath = applicationBuilder.buildApp(projectBo);

        //生成记录
        BuildRecord buildRecord = new BuildRecord();
        buildRecord.setProjectId(projectId);
        buildRecord.setCompanyId(user.getCompanyId());
        buildRecord.setDownloadUrl(""); //TODO
        buildRecord.setName(projectBo.getName());

        Attachment attachment = new Attachment();
        attachment.setCompanyId(user.getCompanyId());
        try {
            File genZipFile = new File(genZipPath);
            attachment.setName(projectBo.getEnName()+"_"+genZipFile.getName());
            byte[] contents = FileUtils.readFileToByteArray(genZipFile);
            attachment.setContent(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
        attachmentService.insert(attachment);

        buildRecord.setAttachmentId(attachment.getId());
        buildRecordService.insert(buildRecord);
        return attachment.getId();
    }




    @Override
    public DbConfigure getDbConfigure(Long projectId) {
        List<Config> configList = configService.selectProjectList(projectId);
        Map<String,String> dataMap = new HashMap<>();
        for (Config config : configList) {
            dataMap.put(config.getName(),config.getValue());
        }
        DbConfigure dbConfigure = new DbConfigure();
        dbConfigure.setDriver(dataMap.get("jdbc.driverClassName"));
        dbConfigure.setUrl(dataMap.get("jdbc.url"));
        dbConfigure.setUser(dataMap.get("jdbc.username"));
        dbConfigure.setPassword(dataMap.get("jdbc.password"));
        return dbConfigure;
    }
}
