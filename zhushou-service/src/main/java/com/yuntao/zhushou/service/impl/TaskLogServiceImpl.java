package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.service.support.bis.QueryBuilderUtils;
import com.yuntao.zhushou.model.query.TaskLogQuery;
import com.yuntao.zhushou.model.vo.TaskLogVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.service.inter.TaskLogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by shan on 2016/8/1.
 */
@Service
public class TaskLogServiceImpl implements TaskLogService {

     Settings settings = Settings.settingsBuilder()
            .put("cluster.name", "ec_cluster").build();

    private Client client;


    @Value("${es.info}")
    private String esInfo;

    private String index = "task_log";

    @PostConstruct
    public void init() {
        InetSocketTransportAddress inetSocketTransportAddress = null;
        List<InetSocketTransportAddress> adressList = new ArrayList<>();
        try {
            String esInfos [] = esInfo.split(",");
            for(String esInfo : esInfos){
                String host = esInfo.split(":")[0];
                Integer port = Integer.valueOf(esInfo.split(":")[1]);
                inetSocketTransportAddress = new InetSocketTransportAddress(InetAddress.getByName(host), port);
                adressList.add(inetSocketTransportAddress);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        TransportClient transportClient = TransportClient.builder().settings(settings).build();
        for(InetSocketTransportAddress address : adressList){
            transportClient.addTransportAddress(address);
        }
        client = transportClient;
    }

    public Pagination<TaskLogVo> selectByPage(TaskLogQuery query) {
        query.setPage(true);
        query.setMaster(true);
        Pagination<TaskLogVo> pagination = selectList(query);
//        List<TaskLogVo> dataList = pagination.getDataList();
//        if(CollectionUtils.isNotEmpty(dataList)){
//            for(TaskLogVo taskLogVo : dataList){
//            }
//        }
        return pagination;
    }

    @Override
    public TaskLogVo findById(String month,String model,String id) {
        TaskLogQuery query = new TaskLogQuery();
        query.setMonth(month);
        query.setModel(model);
        query.setId(id);
        Pagination<TaskLogVo> pagination = selectList(query);
        return pagination.getDataList().get(0);
    }

    @Override
    public Pagination<TaskLogVo> selectListByBatchNo(String month, String model, String batchNo) {
        TaskLogQuery query = new TaskLogQuery();
        query.setMonth(month);
        query.setModel(model);
        query.setBatchNo(batchNo);
        query.setPage(true);
        query.setPageSize(8000);  //足够大,不然出现不能查询全部数据
        Pagination<TaskLogVo> pagination = selectList(query);
        return pagination;
    }


    public Pagination<TaskLogVo> selectList(TaskLogQuery query) {
        if(StringUtils.isEmpty(query.getMonth())){
            query.setMonth(DateUtil.getFmt(new Date().getTime(),"yyyy.MM"));
        }
        String queryIndex = index + "-"+ query.getMonth();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(queryIndex);
        if(query.isPage()){
            int start = (int) ((query.getPageNum() - 1) * query.getPageSize());
            if(start >= 10000){
                throw new BizException("查询范围超过【start <=10000】限制，请输入条件再查询");
            }
            searchRequestBuilder.setFrom(start).setSize(query.getPageSize());
        }
        searchRequestBuilder.setTypes("_type", query.getModel());
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder queryBuilder  = QueryBuilders.boolQuery();
        if(query.isMaster()){
            queryBuilder.must(QueryBuilders.matchPhraseQuery("master",true));
        }

        if (StringUtils.isNotEmpty(query.getId())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("id",query.getId()));
        }
        if (StringUtils.isNotEmpty(query.getKey())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("key",query.getKey()));
        }
        if (StringUtils.isNotEmpty(query.getAppName())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("appName",query.getAppName()));
        }
        if (StringUtils.isNotEmpty(query.getModule())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("module",query.getModule()));
        }
        if(query.getSuccess() != null){
            queryBuilder.must(QueryBuilders.matchPhraseQuery("success",query.getSuccess()));
        }
        if (StringUtils.isNotEmpty(query.getStartTime())) {
            try {
                Date startTime = DateUtils.parseDate(query.getStartTime(), "yyyy-MM-dd HH:mm");
                queryBuilder.must(QueryBuilders.rangeQuery("startTimeLong").gte(startTime.getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotEmpty(query.getEndTime())) {
            try {
                Date endTime = DateUtils.parseDate(query.getEndTime(), "yyyy-MM-dd HH:mm");
                queryBuilder.must(QueryBuilders.rangeQuery("startTimeLong").lte(endTime.getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        //search text
        QueryBuilderUtils.buildQuery(queryBuilder, "batchNo", query.getBatchNoType(), query.getBatchNo());
        QueryBuilderUtils.buildQuery(queryBuilder, "message", query.getMessageType(), query.getMessage());
        QueryBuilderUtils.buildQuery(queryBuilder, "desc", query.getDescType(), query.getDesc());

        searchRequestBuilder.setQuery(queryBuilder);
//        SortBuilder sortBuilder = SortBuilders.fieldSort("timeLong").order(SortOrder.DESC);
        SearchResponse response = searchRequestBuilder.addSort("startTimeLong", SortOrder.DESC).setExplain(true)
                .execute()
                .actionGet();

        SearchHits hits = response.getHits();
        long totalCount = 0;
        List<TaskLogVo> dataList = new ArrayList<>();
        if (hits != null) {
            totalCount = hits.getTotalHits();
            if (totalCount > 0) {
                SearchHit searchHits[] = hits.getHits();
                for (SearchHit searchHit : searchHits) {
                    TaskLogVo taskLogVo = new TaskLogVo();
                    Map<String, Object> beanMap = BeanUtils.beanToMap(taskLogVo);
                    Set<String> keys = beanMap.keySet();
                    for (String key : keys) {
                        Object value = searchHit.getSource().get(key);
                        beanMap.put(key, value);
                    }
                    //copy to bean
                    BeanUtils.mapToBean(beanMap, taskLogVo);
                    //自定义的
//                    hbLogBean.setIndex(searchHit.getIndex());
                    taskLogVo.setId(searchHit.getId());

                    taskLogVo.setLastTime(DateUtil.getRangeTime(DateUtil.getDate(taskLogVo.getTime(), "yyyy-MM-dd HH:mm:ss")));
                    taskLogVo.setStatus(taskLogVo.getSuccess() ? "成功":"失败");
//                    LogWebVo logWebVo = BeanUtils.beanCopy(hbLogBean,LogWebVo.class);
                    dataList.add(taskLogVo);
                }
            }
        }
        Pagination<TaskLogVo> pagination = new Pagination<>(totalCount, query.getPageSize(), (int) query.getPageNum(), dataList);
        return pagination;
    }



}
