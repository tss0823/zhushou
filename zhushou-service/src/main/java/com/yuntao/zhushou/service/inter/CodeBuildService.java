package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.codeBuild.DbConfigure;
import com.yuntao.zhushou.model.domain.codeBuild.Entity;
import com.yuntao.zhushou.model.domain.codeBuild.Property;
import com.yuntao.zhushou.model.param.codeBuild.EntityParam;
import com.yuntao.zhushou.model.query.codeBuild.EntityQuery;
import com.yuntao.zhushou.model.query.codeBuild.PropertyQuery;

import java.util.List;
import java.util.Map;


/**
 * 代码构建服务接口
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
public interface CodeBuildService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<Entity> selectList(EntityQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<Entity> selectPage(EntityQuery query);

    int entitySave(Entity entity) ;

    int entityUpdate(Entity entity) ;

    Entity getEntityByEnName(String enName);

    Entity entityDetail(Long id) ;

    int entityDelete(Long id) ;

    int entityCopy(Long id) ;

    String buildSql(Long appId,String ids) ;

    String buildApp(Long appId,String ids) ;

    List<Property> propertyList(PropertyQuery query) ;
    
    Map<String,String> dataTypeEnums();
    
    int propertySave(EntityParam entityParam) ;

    DbConfigure getDbConfigure(Long appId);

}
