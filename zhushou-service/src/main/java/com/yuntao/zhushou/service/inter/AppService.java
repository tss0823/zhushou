package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.AppQuery;
import com.yuntao.zhushou.model.query.UserQuery;
import com.yuntao.zhushou.model.vo.AppVo;
import com.yuntao.zhushou.model.vo.UserVo;
import com.yuntao.zhushou.model.web.Pagination;

import java.util.List;

public interface AppService {

    App findById(Long id);

    App findByName(String name);

    int updateByName(App app);

    Pagination<AppVo> selectPage(AppQuery query);

    List<App> selectAllList();



}
