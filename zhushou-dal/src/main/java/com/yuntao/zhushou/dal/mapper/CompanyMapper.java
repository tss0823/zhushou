package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.Company;

public interface CompanyMapper extends BaseMapper<Company> {

    int updateByName(App app);


}
