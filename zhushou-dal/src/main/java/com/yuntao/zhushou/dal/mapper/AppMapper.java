package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.App;

public interface AppMapper extends BaseMapper<App> {

    int updateByName(App app);


}
