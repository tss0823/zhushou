package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.Host;
import com.yuntao.zhushou.model.query.HostQuery;

import java.util.List;

public interface HostService {

    Host findById(Long id);

    Pagination<Host> selectPage(HostQuery query);

    List<Host> selectListByAppAndModel(Long appId, String model);

    List<Host> selectListByAppId(Long appId);

    List<Host> selectList(HostQuery query);
    
    /**
     * 新增
     *
     * @param host
     * @return
     */
    int insert(Host host);

    /**
     * 根据id修改
     *
     * @param host
     * @return
     */
    int updateById(Host host);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


}
