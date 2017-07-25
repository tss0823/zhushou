package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.constant.AppConstant;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.AppConfigUtils;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.enums.ProxyContentStatus;
import com.yuntao.zhushou.model.param.DataMap;
import com.yuntao.zhushou.model.query.ProxyContentQuery;
import com.yuntao.zhushou.model.vo.ProxyContentVo;
import com.yuntao.zhushou.service.inter.ProxyEsService;
import com.yuntao.zhushou.service.support.bis.QueryBuilderUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
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
 * Created by shan on 2016/5/5.
 */
@Service
public class ProxyEsServiceImpl extends AbstService implements ProxyEsService {

    Settings settings = Settings.settingsBuilder()
            .put("cluster.name", "ec_cluster").build();

    private Client client;


    @Value("${es.info}")
    private String esInfo;


    private String index = AppConstant.proxyIndex;


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


    @Override
    public ProxyContentVo findById(String month,String model, String id) {
        ProxyContentQuery query = new ProxyContentQuery();
        query.setMonth(month);
        query.setModel(model);
        query.setId(id);
        Pagination<ProxyContentVo> pagination = selectByPage(query);
        List<ProxyContentVo> dataList = pagination.getDataList();
        if(CollectionUtils.isNotEmpty(dataList)){
            return dataList.get(0);
        }
        return null;
    }


    public Pagination<ProxyContentVo> selectByPage(ProxyContentQuery query) {
        if(StringUtils.isEmpty(query.getMonth())){
            query.setMonth(DateUtil.getFmt(new Date().getTime(),"yyyy.MM"));
        }
        String queryIndex = index + "-"+ query.getMonth();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(queryIndex);
//        if(query.isPage()){
            int start = (int) ((query.getPageNum() - 1) * query.getPageSize());
            if(start >= 10000){
                throw new BizException("查询范围超过【start <=10000】限制，请输入条件再查询");
            }
            searchRequestBuilder.setFrom(start).setSize(query.getPageSize());
//        }

        searchRequestBuilder.setTypes("_type", query.getModel());
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder queryBuilder  = QueryBuilders.boolQuery();
//        if(query.isMaster()){
//            queryBuilder.must(QueryBuilders.matchPhraseQuery("master",true));
//        }
        if (StringUtils.isNotEmpty(query.getKey())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("key",query.getKey()));
        }
        if (StringUtils.isNotEmpty(query.getClientIp())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("clientIp",query.getClientIp()));
        }
        if (query.getId() != null) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("id",query.getId()));
        }
        if (query.getUserId() != null) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("userId",query.getUserId()));
        }
        if (query.getStatus() != null) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("status",query.getStatus()));
        }
        if (StringUtils.isNotEmpty(query.getReqTimeStart())) {
            try {
                Date startTime = DateUtils.parseDate(query.getReqTimeStart(), "yyyy-MM-dd HH:mm");
                queryBuilder.must(QueryBuilders.rangeQuery("gmtRequest").gte(startTime.getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotEmpty(query.getReqTimeEnd())) {
            try {
                Date endTime = DateUtils.parseDate(query.getReqTimeEnd(), "yyyy-MM-dd HH:mm");
                queryBuilder.must(QueryBuilders.rangeQuery("gmtRequest").lte(endTime.getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        //search text
        QueryBuilderUtils.buildQuery(queryBuilder, "clientIp", query.getClientIpType(), query.getClientIp());
        QueryBuilderUtils.buildQuery(queryBuilder, "url", query.getUrlType(), query.getUrl());
//        QueryBuilderUtils.buildQuery(queryBuilder, "userAgent", query.getUserAgentType(), query.getUserAgent());

        QueryBuilderUtils.buildQuery(queryBuilder, query.getTextCat(), query.getTextType(), query.getText());
        searchRequestBuilder.setQuery(queryBuilder);
//        SortBuilder sortBuilder = SortBuilders.fieldSort("timeLong").order(SortOrder.DESC);
        SearchResponse response = searchRequestBuilder.addSort("gmtRequest",SortOrder.DESC).setExplain(true)
                .execute()
                .actionGet();

        SearchHits hits = response.getHits();
        long totalCount = 0;
        List<ProxyContentVo> dataList = new ArrayList<>();
        if (hits != null) {
            totalCount = hits.getTotalHits();
            if (totalCount > 0) {
                SearchHit searchHits[] = hits.getHits();
                for (SearchHit searchHit : searchHits) {
                    ProxyContentVo hbLogBean = new ProxyContentVo();
                    Map<String, Object> beanMap = BeanUtils.beanToMap(hbLogBean);
                    Set<String> keys = beanMap.keySet();
                    for (String key : keys) {
                        Object value = searchHit.getSource().get(key);
                        beanMap.put(key, value);
                    }
                    //copy to bean
                    BeanUtils.mapToBean(beanMap, hbLogBean);
                    //自定义的
                    hbLogBean.setIndex(searchHit.getIndex());
                    hbLogBean.setId(searchHit.getId());

                    ProxyContentVo logWebVo = BeanUtils.beanCopy(hbLogBean,ProxyContentVo.class);
                    ProxyContentStatus proxyContentStatus = ProxyContentStatus.getByCode(logWebVo.getStatus());
                    if(proxyContentStatus != null){
                        logWebVo.setStatusText(proxyContentStatus.getDescription());
                    }
                    logWebVo.setLastReqTime(DateUtil.getRangeTime(logWebVo.getGmtRequest(), "yyyy-MM-dd HH:mm:ss"));
                    logWebVo.setLastResTime(DateUtil.getRangeTime(logWebVo.getGmtResponse(), "yyyy-MM-dd HH:mm:ss"));

                    //reqDataText
                            logWebVo.setReqDataText(logWebVo.getReqData());
//                    byte[] reqData = logWebVo.getReqData();
//                    if(reqData != null && reqData.length > 0){
//                        try{
//                            logWebVo.setReqDataText(new String(reqData,"utf-8"));
//                        }catch (Exception e){
//                        }
//                    }
                    //end


                    //resDataText

                    String resData = logWebVo.getResData();
                    if (StringUtils.isNotEmpty(resData)) {
                        if(StringUtils.indexOf(logWebVo.getResContentType(),"json") != -1){
                            //json 格式化
                            String formatData = JsonUtils.format(resData);
                            logWebVo.setResDataText(formatData);
                        }else{
                            logWebVo.setResDataText(resData);
                        }
                    }
                    //end

                    //header
                    String reqHeader = logWebVo.getReqHeader();
                    logWebVo.setReqHeaderList(new ArrayList<DataMap>());
                    Map<String,String> map = JsonUtils.json2Object(reqHeader, HashMap.class);
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        DataMap dataMap = new DataMap();
                        dataMap.setKey(entry.getKey());
                        dataMap.setValue(entry.getValue());
                        logWebVo.getReqHeaderList().add(dataMap);
                    }

                    String resHeader = logWebVo.getResHeader();
                    if(StringUtils.isNotEmpty(resHeader)){
                        logWebVo.setResHeaderList(new ArrayList<DataMap>());
                        map = JsonUtils.json2Object(resHeader, HashMap.class);
                        entries = map.entrySet();
                        for (Map.Entry<String, String> entry : entries) {
                            DataMap dataMap = new DataMap();
                            dataMap.setKey(entry.getKey());
                            dataMap.setValue(entry.getValue());
                            logWebVo.getResHeaderList().add(dataMap);
                        }
                    }
                    //end

                    dataList.add(logWebVo);
                }
            }
        }
        Pagination<ProxyContentVo> pagination = new Pagination<>(totalCount, query.getPageSize(), (int) query.getPageNum(), dataList);
        return pagination;
    }


    public void addPorxyContent(ProxyContent proxyContent){
        String curentMonth = DateUtil.getFmt(new Date().getTime(), "yyyy.MM");
        String model = AppConfigUtils.getValue("model");
        if (StringUtils.isEmpty(model)) {
            model = "test";
        }
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(AppConstant.proxyIndex + "-" + curentMonth, model);

        String json = JsonUtils.object2Json(proxyContent);
        IndexResponse indexResponse = indexRequestBuilder.setSource(json).get();
        String id = indexResponse.getId();
        boolean created = indexResponse.isCreated();
        log.info("crated="+created+",id="+id);
    }
}
