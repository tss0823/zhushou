/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.ProxyReqFilterMapper;
import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.domain.ProxyReqFilter;
import com.yuntao.zhushou.model.domain.ProxyReqFilterItem;
import com.yuntao.zhushou.model.enums.*;
import com.yuntao.zhushou.model.query.ProxyReqFilterQuery;
import com.yuntao.zhushou.model.vo.ProxyReqFilterVo;
import com.yuntao.zhushou.service.inter.ProxyReqFilterItemService;
import com.yuntao.zhushou.service.inter.ProxyReqFilterService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ProxyReqFilterServiceImpl extends AbstService implements ProxyReqFilterService {


    @Autowired
    private ProxyReqFilterMapper proxyReqFilterMapper;

    @Autowired
    private ProxyReqFilterItemService proxyReqFilterItemService;

    @Override
    public List<ProxyReqFilter> selectList(ProxyReqFilterQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return proxyReqFilterMapper.selectList(queryMap);
    }

    @Override
    public ProxyReqFilter selectOne(ProxyReqFilterQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<ProxyReqFilter> proxyReqFilters = proxyReqFilterMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(proxyReqFilters)) {
            return proxyReqFilters.get(0);
        }
        return null;
    }

    @Override
    public Pagination<ProxyReqFilterVo> selectPage(ProxyReqFilterQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = proxyReqFilterMapper.selectListCount(queryMap);
        Pagination<ProxyReqFilterVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<ProxyReqFilter> dataList = proxyReqFilterMapper.selectList(queryMap);
        List<ProxyReqFilterVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (ProxyReqFilter proxyReqFilter : dataList) {
            ProxyReqFilterVo proxyReqFilterVo = BeanUtils.beanCopy(proxyReqFilter, ProxyReqFilterVo.class);
            newDataList.add(proxyReqFilterVo);
        }
        return pagination;
    }

    @Override
    public ProxyReqFilter findById(Long id) {
        return proxyReqFilterMapper.findById(id);
    }


    @Override
    public int insert(ProxyReqFilter proxyReqFilter) {
        return proxyReqFilterMapper.insert(proxyReqFilter);
    }

    @Override
    public int updateById(ProxyReqFilter proxyReqFilter) {
        return proxyReqFilterMapper.updateById(proxyReqFilter);
    }

    @Override
    public int deleteById(Long id) {
        return proxyReqFilterMapper.deleteById(id);
    }

    @Override
    public boolean match(ProxyContent proxyContent) {
        //获取素有有效的请求过滤数据
        ProxyReqFilterQuery query = new ProxyReqFilterQuery();
        query.setStatus(YesNoIntType.yes.getCode());
        List<ProxyReqFilter> proxyReqFilterList = this.selectList(query);
        if (CollectionUtils.isEmpty(proxyReqFilterList)) {  //没有数据，忽略
            return true;
        }
        for (ProxyReqFilter proxyReqFilter : proxyReqFilterList) {
            Integer joinType = proxyReqFilter.getJoinType();
            boolean joinFlag = joinType == ProxyFilterJoinType.and.getCode() ? true : false;

            //然后获取他们对应的子集
            List<ProxyReqFilterItem> proxyReqFilterItemList = proxyReqFilterItemService.selectByParentId(ProxyReqFilterItemFilterType.req.getCode(),proxyReqFilter.getId());
            if (CollectionUtils.isEmpty(proxyReqFilterItemList)) {
                continue;
            }

            if (this.checkChildItemFilter(proxyContent,joinFlag,proxyReqFilterItemList)) {  //只要有一项通过，就ok
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkChildItemFilter(ProxyContent proxyContent, boolean joinFlag, List<ProxyReqFilterItem> proxyReqFilterItemList) {
        boolean allCheckOk = true;
        for (ProxyReqFilterItem proxyReqFilterItem : proxyReqFilterItemList) {
            if (proxyReqFilterItem.getStatus() == YesNoIntType.no.getCode()) {
                continue;
            }
            Integer type = proxyReqFilterItem.getType();
            String matchType = proxyReqFilterItem.getMatchType();
            String key = proxyReqFilterItem.getKey();
            String value = proxyReqFilterItem.getValue();

            if (type.intValue() == ProxyReqFilterType.url.getCode()) { //url
                String methodName = "get" + StringUtils.capitalize(key);
                try {
                    Method method = ClassUtils.getPublicMethod(ProxyContent.class, methodName);
                    Object actualValue = method.invoke(proxyContent);
                    boolean checkResult = this.checkResult(matchType, String.valueOf(actualValue), value);
                    if(!checkResult){
                        allCheckOk = false;
                    }
                    if (checkResult && !joinFlag) {  //check true and joinType is or
                        return true;
                    } else if (!checkResult && joinFlag) { //check false and joinType is and
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (type.intValue() == ProxyReqFilterType.header.getCode()) { //header
                String reqHeader = proxyContent.getReqHeader();
                Map<String, String> reqHeaderMap = JsonUtils.json2Object(reqHeader, Map.class);
                String actualValue = reqHeaderMap.get(key);
                boolean checkResult = this.checkResult(matchType, String.valueOf(actualValue), value);
                if(!checkResult){
                    allCheckOk = false;
                }
                if (checkResult && !joinFlag) {  //check true and joinType is or
                    return true;
                } else if (!checkResult && joinFlag) { //check false and joinType is and
                    return false;
                }
            } else { //body
                String actualValue = proxyContent.getReqData();
                boolean checkResult = this.checkResult(matchType, String.valueOf(actualValue), value);
                if(!checkResult){
                    allCheckOk = false;
                }
                if (checkResult && !joinFlag) {  //check true and joinType is or
                    return true;
                } else if (!checkResult && joinFlag) { //check false and joinType is and
                    return false;
                }
            }
        }
        return allCheckOk;
    }


    private boolean checkResult(String matchType, String actualValue, String compareValue) {
        if (matchType.equals(ProxyMatchType.eq.getCode())) {
            return (StringUtils.isNotEmpty(actualValue) && actualValue.equals(compareValue));
        } else if (matchType.equals(ProxyMatchType.neq.getCode())) {
            return (StringUtils.isEmpty(actualValue) || !actualValue.equals(compareValue));

        } else if (matchType.equals(ProxyMatchType.include.getCode())) {
            return (StringUtils.isNotEmpty(actualValue) && actualValue.contains(compareValue));

        } else if (matchType.equals(ProxyMatchType.exclude.getCode())) {
            return (StringUtils.isEmpty(actualValue) || !actualValue.contains(compareValue));

        } else if (matchType.equals(ProxyMatchType.regex.getCode())) {
            Pattern pattern = Pattern.compile(compareValue);
            Matcher matcher = pattern.matcher(actualValue);
            return matcher.find();
        } else {
            throw new BizException("not implement matchType=" + matchType);
        }
    }


}