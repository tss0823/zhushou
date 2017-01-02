package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.DeployLog;
import com.yuntao.zhushou.model.query.DeployLogQuery;
import com.yuntao.zhushou.model.vo.DeployLogVo;
import com.yuntao.zhushou.common.web.Pagination;

import java.util.List;

public interface DeployLogService {

    DeployLog findById(Long id);

    DeployLogVo findDetailById(Long id);

    Pagination<DeployLogVo> selectPage(DeployLogQuery query);

    int insert(DeployLog deployLog);

    List<DeployLog> selectList(DeployLogQuery query);



}
