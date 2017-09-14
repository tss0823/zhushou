/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.CodeBuildSqlMapper;
import com.yuntao.zhushou.model.domain.CodeBuildSql;
import com.yuntao.zhushou.model.query.CodeBuildSqlQuery;
import com.yuntao.zhushou.model.vo.CodeBuildSqlVo;
import com.yuntao.zhushou.service.inter.CodeBuildSqlService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("codeBuildSql")
public class CodeBuildSqlServiceImpl extends AbstService implements CodeBuildSqlService {


    @Autowired
    private CodeBuildSqlMapper codeBuildSqlMapper;

    @Override
    public List<CodeBuildSql> selectList(CodeBuildSqlQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return codeBuildSqlMapper.selectList(queryMap);
    }

    @Override
    public CodeBuildSql selectOne(CodeBuildSqlQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<CodeBuildSql> codeBuildSqls = codeBuildSqlMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(codeBuildSqls)) {
            return codeBuildSqls.get(0);
        }
        return null;
    }

    @Override
    public Pagination<CodeBuildSqlVo> selectPage(CodeBuildSqlQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = codeBuildSqlMapper.selectListCount(queryMap);
        Pagination<CodeBuildSqlVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<CodeBuildSql> dataList = codeBuildSqlMapper.selectList(queryMap);
        List<CodeBuildSqlVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (CodeBuildSql codeBuildSql : dataList) {
            CodeBuildSqlVo codeBuildSqlVo = BeanUtils.beanCopy(codeBuildSql, CodeBuildSqlVo.class);
            newDataList.add(codeBuildSqlVo);
        }
        return pagination;
    }

    @Override
    public CodeBuildSql findById(Long id) {
        return codeBuildSqlMapper.findById(id);
    }


    @Override
    public int insert(CodeBuildSql codeBuildSql) {
        return codeBuildSqlMapper.insert(codeBuildSql);
    }

    @Override
    public int updateById(CodeBuildSql codeBuildSql) {
        return codeBuildSqlMapper.updateById(codeBuildSql);
    }

    @Override
    public int deleteById(Long id) {
        return codeBuildSqlMapper.deleteById(id);
    }


}