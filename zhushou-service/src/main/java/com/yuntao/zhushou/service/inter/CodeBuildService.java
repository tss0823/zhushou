package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.domain.codeBuild.DbConfigure;

import java.util.List;


/**
 * 代码构建服务接口
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
public interface CodeBuildService {

    int entityCopy(Long id) ;

    String buildSql(User user,Long projectId, List<Long> entityIds);

    Long buildApp(boolean isSingle,User user, Long projectId, List<Long> entityIds) ;

    DbConfigure getDbConfigure(Long projectId);

}
