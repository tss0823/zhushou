package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.dal.mapper.IdocParamMapper;
import com.yuntao.zhushou.model.domain.IdocParam;
import com.yuntao.zhushou.model.query.IdocParamQuery;
import com.yuntao.zhushou.model.vo.IdocParamVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.service.inter.IdocParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 接口请求参数服务实现类
 * 
 * @author admin
 *
 * @2016-07-30 20
 */
@Service("idocParamService")
public class IdocParamServiceImpl implements IdocParamService {

    private final  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdocParamMapper idocParamMapper;

    @Override
    public List<IdocParam> selectList(IdocParamQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return idocParamMapper.selectList(queryMap);
    }

    @Override
    public Pagination<IdocParamVo> selectPage(IdocParamQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = idocParamMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<IdocParam> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        queryMap.put("pagination",pagination);
        List<IdocParam> dataList = idocParamMapper.selectList(queryMap);
        Pagination<IdocParamVo> newPageInfo = new Pagination<>(pagination);
        List<IdocParamVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for(IdocParam idocParam : dataList){
            IdocParamVo idocParamVo = BeanUtils.beanCopy(idocParam,IdocParamVo.class);
            newDataList.add(idocParamVo);
        }
        return newPageInfo;

    }

    @Override
    public IdocParam findById(Long id) {
        return idocParamMapper.findById(id);
    }


    @Override
    public int insert(IdocParam idocParam) {
        return idocParamMapper.insert(idocParam);
    }

    @Override
    public int updateById(IdocParam idocParam) {
        return idocParamMapper.updateById(idocParam);
    }

    @Override
    public int deleteById(Long id) {
        return idocParamMapper.deleteById(id);
    }

    @Override
    public int deleteByParentId(Long id) {
        return idocParamMapper.deleteByParentId(id);
    }

    @Override
    public List<IdocParam> selectByParentId(Long parentId) {
        IdocParamQuery query = new IdocParamQuery();
        query.setParentId(parentId);
        return selectList(query);
    }

}
