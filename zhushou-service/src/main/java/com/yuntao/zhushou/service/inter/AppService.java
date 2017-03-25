package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.query.AppQuery;
import com.yuntao.zhushou.model.vo.AppVo;
import com.yuntao.zhushou.common.web.Pagination;

import java.util.List;

public interface AppService {

    App findById(Long id);

    App findByName(Long companyId, String name);

    int updateLog(Long companyId,String appName,String log);

    Pagination<AppVo> selectPage(AppQuery query);

    Pagination<AppVo> selectFrontPage(AppQuery query);

    List<App> selectAllList();

    List<App> selectList(AppQuery query);

    List<App> selectByCompanyId(Long companyId);



}
