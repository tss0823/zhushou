package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.model.query.AppQuery;
import com.yuntao.zhushou.model.query.CompanyQuery;
import com.yuntao.zhushou.model.query.CompanyQuery;
import com.yuntao.zhushou.model.vo.AppVo;
import com.yuntao.zhushou.model.vo.CompanyVo;
import com.yuntao.zhushou.model.vo.CompanyVo;

import java.util.List;

public interface CompanyService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<Company> selectList(CompanyQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<CompanyVo> selectPage(CompanyQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    Company findById(Long id);

    /**
     * 根据id获得对象
     * @param key
     * @return
     */
    Company findByKey(String key);

    /**
     * 新增
     * @param company
     * @return
     */
    int insert(Company company) ;

    /**
     * 根据id修改
     * @param company
     * @return
     */
    int updateById(Company company);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);



}
