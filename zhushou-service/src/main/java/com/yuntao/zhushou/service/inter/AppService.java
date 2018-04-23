package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.query.AppQuery;
import com.yuntao.zhushou.model.vo.AppVo;

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

    /**
     * 新增
     *
     * @param project
     * @return
     */
    int insert(App project);


    /**
     * 根据id修改
     *
     * @param project
     * @return
     */
    int updateById(App project);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


    int saveOrUpdate(App app,List<Long> testHostIds,List<Long> prodHostIds);

}
