package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.AppFront;
import com.yuntao.zhushou.model.domain.AppVersion;
import com.yuntao.zhushou.model.query.AppFrontQuery;
import com.yuntao.zhushou.model.vo.AppFrontVo;

import java.util.List;

public interface AppFrontService {

    AppFront findById(Long id);

    AppFront findByName(Long companyId, String name);

    int updateLog(Long companyId, String appName, String log);

    Pagination<AppFrontVo> selectPage(AppFrontQuery query);

    Pagination<AppFrontVo> selectFrontPage(AppFrontQuery query);

//    List<AppFront> selectAllList();
//
    List<AppFront> selectList(AppFrontQuery query);

    List<AppFront> selectByCompanyId(Long companyId);

    void deploy(Long appFrontId, AppVersion appVersion);



}
