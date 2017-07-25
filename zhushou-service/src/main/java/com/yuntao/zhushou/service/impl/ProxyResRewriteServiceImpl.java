/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.ProxyResRewriteMapper;
import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.domain.ProxyReqFilterItem;
import com.yuntao.zhushou.model.domain.ProxyResRewrite;
import com.yuntao.zhushou.model.enums.ProxyFilterJoinType;
import com.yuntao.zhushou.model.enums.ProxyReqFilterItemFilterType;
import com.yuntao.zhushou.model.enums.YesNoIntType;
import com.yuntao.zhushou.model.query.ProxyResRewriteQuery;
import com.yuntao.zhushou.model.vo.ProxyResRewriteVo;
import com.yuntao.zhushou.service.inter.ProxyReqFilterItemService;
import com.yuntao.zhushou.service.inter.ProxyReqFilterService;
import com.yuntao.zhushou.service.inter.ProxyResRewriteService;
import com.yuntao.zhushou.service.support.proxy.HttpExecuteTask;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class ProxyResRewriteServiceImpl extends AbstService implements ProxyResRewriteService {


    @Autowired
    private ProxyResRewriteMapper proxyResRewriteMapper;

    @Autowired
    private ProxyReqFilterService proxyReqFilterService;

    @Autowired
    private ProxyReqFilterItemService proxyReqFilterItemService;

    @Override
    public List<ProxyResRewrite> selectList(ProxyResRewriteQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return proxyResRewriteMapper.selectList(queryMap);
    }

    @Override
    public ProxyResRewrite selectOne(ProxyResRewriteQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<ProxyResRewrite> proxyResRewrites = proxyResRewriteMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(proxyResRewrites)) {
            return proxyResRewrites.get(0);
        }
        return null;
    }

    @Override
    public Pagination<ProxyResRewriteVo> selectPage(ProxyResRewriteQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = proxyResRewriteMapper.selectListCount(queryMap);
        Pagination<ProxyResRewriteVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<ProxyResRewrite> dataList = proxyResRewriteMapper.selectList(queryMap);
        List<ProxyResRewriteVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (ProxyResRewrite proxyResRewrite : dataList) {
            ProxyResRewriteVo proxyResRewriteVo = BeanUtils.beanCopy(proxyResRewrite, ProxyResRewriteVo.class);
            newDataList.add(proxyResRewriteVo);
        }
        return pagination;
    }

    @Override
    public ProxyResRewrite findById(Long id) {
        return proxyResRewriteMapper.findById(id);
    }


    @Override
    public int insert(ProxyResRewrite proxyResRewrite) {
        return proxyResRewriteMapper.insert(proxyResRewrite);
    }

    @Override
    public int updateById(ProxyResRewrite proxyResRewrite) {
        return proxyResRewriteMapper.updateById(proxyResRewrite);
    }

    @Override
    public int deleteById(Long id) {
        return proxyResRewriteMapper.deleteById(id);
    }

    @Override
    public boolean rewriteProcess(HttpExecuteTask httpExecuteTask) {
        ProxyContent proxyContent = httpExecuteTask.getProxyContent();

        //获取素有有效的返回重写数据,找到第一个就替换，最新原则
        ProxyResRewriteQuery query = new ProxyResRewriteQuery();
        query.setStatus(YesNoIntType.yes.getCode());
        List<ProxyResRewrite> proxyResRewriteList = this.selectList(query);
        boolean result = false;
        if (CollectionUtils.isEmpty(proxyResRewriteList)) {  //没有数据，忽略
            result = true;
            return result;
        }
        for (ProxyResRewrite proxyResRewrite : proxyResRewriteList) {
            Integer joinType = proxyResRewrite.getJoinType();
            boolean joinFlag = joinType == ProxyFilterJoinType.and.getCode() ? true : false;

            //然后获取他们对应的子集
            List<ProxyReqFilterItem> proxyReqFilterItemList = proxyReqFilterItemService.selectByParentId(ProxyReqFilterItemFilterType.rewrite.getCode(),proxyResRewrite.getId());
            if (CollectionUtils.isEmpty(proxyReqFilterItemList)) {
                continue;
            }

            if (proxyReqFilterService.checkChildItemFilter(proxyContent,joinFlag,proxyReqFilterItemList)) {  //只要有一项通过，就ok

                Integer resModel = proxyResRewrite.getResModel();
                if(resModel.intValue() == 0){  //静态
                    Integer resType = proxyResRewrite.getResType();  //返回类型
                    String replaceData = proxyResRewrite.getData();
                    if(resType.intValue() == 0){  //header
                        httpExecuteTask.execute();  //执行请求

                        String resHeader = proxyContent.getResHeader();
                        Map<String,String> resHeaderMap = JsonUtils.json2Object(resHeader, Map.class);

                        if(resHeaderMap != null){
                            Map<String,String> replaceResHeaderMap = JsonUtils.json2Object(replaceData, Map.class);
                            Set<Map.Entry<String, String>> replaceResHeaderEntry = replaceResHeaderMap.entrySet();
                            for (Map.Entry<String, String> replaceResEntry : replaceResHeaderEntry) {
                                resHeaderMap.put(replaceResEntry.getKey(),replaceResEntry.getValue());
                            }
                            String newResHeader = JsonUtils.object2Json(resHeaderMap);
                            proxyContent.setResHeader(newResHeader);
                        }

                        httpExecuteTask.writeToClient();


                    }else{ //body,如果只是替换body ，则不请求url
                        Map<String,String> resHeaderMap = new HashMap<>();
                        resHeaderMap.put("Content-length",""+replaceData.getBytes().length);
                        String resHeader = JsonUtils.object2Json(resHeaderMap);
                        proxyContent.setResHeader(resHeader);
                        proxyContent.setResData(replaceData);
                        proxyContent.setHttpStatus(200);
                        httpExecuteTask.writeToClient();
                    }

                }else{  //动态 TODO

                }
                return true;
            }
        }
        return result;
    }

    @Transactional
    @Override
    public Long addRewrite(ProxyResRewrite proxyResRewrite, List<ProxyReqFilterItem> itemList) {
        this.insert(proxyResRewrite);

        //save item
        for (ProxyReqFilterItem proxyReqFilterItem : itemList) {
            proxyReqFilterItem.setParentId(proxyResRewrite.getId());
            proxyReqFilterItem.setStatus(YesNoIntType.yes.getCode());
            proxyReqFilterItem.setFilterType(ProxyReqFilterItemFilterType.rewrite.getCode());
            this.proxyReqFilterItemService.insert(proxyReqFilterItem);
        }
        return proxyResRewrite.getId();
    }


}