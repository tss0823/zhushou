package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.CompanyMapper;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.model.query.CompanyQuery;
import com.yuntao.zhushou.model.vo.CompanyVo;
import com.yuntao.zhushou.service.inter.CompanyService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 企业服务实现类
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public List<Company> selectList(CompanyQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return companyMapper.selectList(queryMap);
    }

    @Override
    public Pagination<CompanyVo> selectPage(CompanyQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = companyMapper.selectListCount(queryMap);
        Pagination<CompanyVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination",pagination);
        List<Company> dataList = companyMapper.selectList(queryMap);
//        Pagination<CompanyVo> newPageInfo = new Pagination<>(pagination);
        List<CompanyVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
//        pagination.setDataList(dataList);
        for(Company company : dataList){
            CompanyVo companyVo = BeanUtils.beanCopy(company,CompanyVo.class);
            newDataList.add(companyVo);
        }
        return pagination;

    }

    @Override
    public Company findById(Long id) {
        return companyMapper.findById(id);
    }

    @Override
    public Company findByKey(String key) {
        CompanyQuery companyQuery = new CompanyQuery();
        companyQuery.setKey(key);
        List<Company> companyList = selectList(companyQuery);
        if(CollectionUtils.isNotEmpty(companyList)){
            return companyList.get(0);
        }
        return null;
    }


    @Override
    public int insert(Company company) {
        return companyMapper.insert(company);
    }

    @Override
    public int updateById(Company company) {
        return companyMapper.updateById(company);
    }

    @Override
    public int deleteById(Long id) {
        return companyMapper.deleteById(id);
    }

}
