package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.Host;
import com.yuntao.zhushou.model.query.HostQuery;
import com.yuntao.zhushou.model.web.Pagination;

import java.util.List;

public interface HostService {

    Host findById(Long id);

    Host findByName(String name);


    Pagination<Host> selectPage(HostQuery query);

    List<Host> selectListByAppAndModel(Long appId, String model);

    List<Host> selectListByAll();


}
