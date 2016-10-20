package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.*;
import com.yuntao.zhushou.model.enums.LogLevel;
import com.yuntao.zhushou.model.query.LogQuery;
import com.yuntao.zhushou.model.query.LogTextQuery;
import com.yuntao.zhushou.model.vo.LogVo;
import com.yuntao.zhushou.model.vo.LogWebVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.LogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.mapper.object.ObjectMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by shan on 2016/5/5.
 */
@Service
public class LogServiceImpl extends AbstService implements LogService {

    Settings settings = Settings.settingsBuilder()
            .put("cluster.name", "ec_cluster").build();

    private Client client;


    @Value("${es.info}")
    private String esInfo;


    private String index = "stack_log";

    private Queue<String> yunPanLogQueue = new ConcurrentLinkedQueue<>();
    private Queue<LogWebVo> logList = new ConcurrentLinkedQueue<>();

    private AtomicLong consumerCount = new AtomicLong();
    private AtomicLong logPageNum = new AtomicLong();

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

    public Pagination<LogWebVo> selectByPage(LogQuery query, LogTextQuery logTextQuery) {
        query.setPage(true);
        query.setMaster(true);
        Pagination<LogWebVo> pagination = selectList(query,logTextQuery);
        List<LogWebVo> dataList = pagination.getDataList();
        if(CollectionUtils.isNotEmpty(dataList)){
            for(LogWebVo logWebVo : dataList){
                if(StringUtils.equals(logWebVo.getLevel(), LogLevel.BIZ_ERROR.getCode())){
                    Map<String,Object> map  = JsonUtils.json2Object(logWebVo.getResponse(), HashMap.class);
                    if(map != null){
                        Object errMsg = map.get("message");
                        if(errMsg != null){
                            logWebVo.setErrMsg(errMsg.toString());
                        }
                    }
                }
            }
        }
        return pagination;
    }

    @Override
    public List<LogWebVo> selectListByStackId(String month,String model,String stackId) {
        LogQuery query = new LogQuery();
        query.setMonth(month);
        query.setModel(model);
        query.setStackId(stackId);
        query.setPage(true);
        query.setPageSize(1000);  //足够大,不然出现不能查询全部数据
        Pagination<LogWebVo> pagination = selectList(query,new LogTextQuery());
        return pagination.getDataList();
    }

    @Override
    public LogWebVo findMasterByStackId(String month,String model, String stackId) {
        LogQuery query = new LogQuery();
        query.setMonth(month);
        query.setModel(model);
        query.setStackId(stackId);
        query.setMaster(true);
        Pagination<LogWebVo> pagination = selectList(query,new LogTextQuery());
        List<LogWebVo> dataList = pagination.getDataList();
        if(CollectionUtils.isNotEmpty(dataList)){
            return dataList.get(0);
        }
        return null;
    }


    public Pagination<LogWebVo> selectList(LogQuery query, LogTextQuery logTextQuery) {
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
        if (StringUtils.isNotEmpty(query.getStackId())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("stackId",query.getStackId()));
        }
        if (StringUtils.isNotEmpty(query.getAppName())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("appName",query.getAppName()));
        }
        if (StringUtils.isNotEmpty(query.getLevel())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("level",query.getLevel()));
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
        if (StringUtils.isNotEmpty(query.getStartTime())) {
            try {
                Date startTime = DateUtils.parseDate(query.getStartTime(), "yyyy-MM-dd HH:mm");
                queryBuilder.must(QueryBuilders.rangeQuery("timeLong").gte(startTime.getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotEmpty(query.getEndTime())) {
            try {
                Date endTime = DateUtils.parseDate(query.getEndTime(), "yyyy-MM-dd HH:mm");
                queryBuilder.must(QueryBuilders.rangeQuery("timeLong").lte(endTime.getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        //search text
        QueryBuilderUtils.buildQuery(queryBuilder, "mobile", logTextQuery.getMobileType(), logTextQuery.getMobile());
        QueryBuilderUtils.buildQuery(queryBuilder, "url", logTextQuery.getUrlType(), logTextQuery.getUrl());
        QueryBuilderUtils.buildQuery(queryBuilder, "userAgent", logTextQuery.getUserAgentType(), logTextQuery.getUserAgent());

        QueryBuilderUtils.buildQuery(queryBuilder, logTextQuery.getTextCat(), logTextQuery.getTextType(), logTextQuery.getText());
        searchRequestBuilder.setQuery(queryBuilder);
//        SortBuilder sortBuilder = SortBuilders.fieldSort("timeLong").order(SortOrder.DESC);
        SearchResponse response = searchRequestBuilder.addSort("timeLong",SortOrder.DESC).setExplain(true)
                .execute()
                .actionGet();

        SearchHits hits = response.getHits();
        long totalCount = 0;
        List<LogWebVo> dataList = new ArrayList<>();
        if (hits != null) {
            totalCount = hits.getTotalHits();
            if (totalCount > 0) {
                SearchHit searchHits[] = hits.getHits();
                for (SearchHit searchHit : searchHits) {
                    LogVo hbLogBean = new LogVo();
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

                    hbLogBean.setLastTime(DateUtil.getRangeTime(DateUtil.getDate(hbLogBean.getTime(), "yyyy-MM-dd HH:mm:ss")));
                    LogWebVo logWebVo = BeanUtils.beanCopy(hbLogBean,LogWebVo.class);
                    dataList.add(logWebVo);
                }
            }
        }
        Pagination<LogWebVo> pagination = new Pagination<>(totalCount, query.getPageSize(), (int) query.getPageNum(), dataList);
        return pagination;
    }

    public void delHisDataTask(){
        //获取数据任务
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        //prod
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    long startTime = System.currentTimeMillis();
                    bisLog.info("get prod log start starTime="+startTime);
                    List<LogWebVo> totalDataList = new ArrayList();
                    //
                    String month =  DateUtil.getFmt(new Date().getTime(),"yyyy.MM");
                    List<LogWebVo> dataList = getLogDataList(month,"prod",logPageNum.getAndIncrement());
                    if(CollectionUtils.isNotEmpty(dataList)){
                        totalDataList.addAll(dataList);
                    }

                    //last month
                    Date lastMonthDay = DateUtil.getMonthAfter(new Date(),-1);
                    month =  DateUtil.getFmt(lastMonthDay.getTime(),"yyyy.MM");
                    List<LogWebVo> dataLastList = getLogDataList(month,"prod",logPageNum.getAndIncrement());
                    if(CollectionUtils.isNotEmpty(dataLastList)){
                        totalDataList.addAll(dataLastList);
                    }
                    for(LogWebVo logWebVo : totalDataList){
                        yunPanLogQueue.offer(JsonUtils.object2Json(logWebVo));
                    }
                    long endTime = System.currentTimeMillis();
                    bisLog.info("get prod log end take time="+(endTime-startTime)+" ms,get data size="+totalDataList.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1, 20, TimeUnit.SECONDS);

        //test
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    long startTime = System.currentTimeMillis();
                    bisLog.info("get test log start starTime="+startTime);
                    List<LogWebVo> totalDataList = new ArrayList();
                    //
                    String month =  DateUtil.getFmt(new Date().getTime(),"yyyy.MM");
                    List<LogWebVo> dataList = getLogDataList(month,"test",logPageNum.getAndIncrement());
                    if(CollectionUtils.isNotEmpty(dataList)){
                        totalDataList.addAll(dataList);
                    }

                    //last month
                    Date lastMonthDay = DateUtil.getMonthAfter(new Date(),-1);
                    month =  DateUtil.getFmt(lastMonthDay.getTime(),"yyyy.MM");
                    List<LogWebVo> dataLastList = getLogDataList(month,"test",logPageNum.getAndIncrement());
                    if(CollectionUtils.isNotEmpty(dataLastList)){
                        totalDataList.addAll(dataLastList);
                    }
                    for(LogWebVo logWebVo : totalDataList){
                        logList.offer(logWebVo);
                    }
                    long endTime = System.currentTimeMillis();
                    bisLog.info("get test log end take time="+(endTime-startTime)+" ms,get data size="+totalDataList.size());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1, 60 * 5, TimeUnit.SECONDS);

        //备份云盘数据
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    long startTime = System.currentTimeMillis();
                    bisLog.info("upload yunPan back log starTime="+startTime);
                    int yunPanFileSize = 5000;
                    StringBuilder sb = new StringBuilder();
                    while(yunPanFileSize > 0 && !yunPanLogQueue.isEmpty()){
                        sb.append(yunPanLogQueue.poll());
                    }
                    if(sb.length() == 0){
                        bisLog.info("upload yunPan back log end for empty");
                        return;
                    }
                    String time = DateUtil.getFmtyMdHmNoSymbol(new Date().getTime());
                    String file = time + ".txt";
                    HttpFileUploadUtils.uploadYunPan(file,sb.toString().getBytes());

                    long endTime = System.currentTimeMillis();
                    bisLog.info("upload yunPan back log end take time="+(endTime-startTime)+" ms,fileName="+file);
                    bisLog.info("get log size="+logList.size()+",delete log size="+yunPanLogQueue.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1, 50, TimeUnit.SECONDS);

        //删除数据任务
        final long startMainTime = System.currentTimeMillis();
        ExecutorService ec = new ThreadPoolExecutor(4, 4, 0, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>()){

            @Override protected void terminated() {
                super.terminated();
                bisLog.info("finish, take time = " + (System.currentTimeMillis() - startMainTime));
            }
        };
        while(true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ec.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        LogWebVo logWebVo = logList.poll();
                        if(logWebVo == null){
                            return;
                        }
                        client.prepareDelete(logWebVo.getIndex(),logWebVo.getType(),logWebVo.getId())
                                .execute().actionGet();
//                        consumerCount.incrementAndGet();
//                        consumerCount.set(0);
                        yunPanLogQueue.offer(JsonUtils.object2Json(logWebVo));

                    }catch (Exception e){
                        log.info("remove index failed!",e);
                    }
                }
            });
        }
    }

    private List<LogWebVo> getLogDataList(String month,String model,long pageNum){
        LogQuery logQuery = new LogQuery();
        logQuery.setPage(true);
        logQuery.setPageNum(pageNum);
        logQuery.setMonth(month);
        logQuery.setModel(model);
        Date hisDate = DateUtil.getDateAfter(new Date(),-7);
//        Long endTime = hisDate.getTime();
        logQuery.setEndTime(DateFormatUtils.format(hisDate,"yyyy-MM-dd HH:mm:ss"));
        int pageSize = 500;
        logQuery.setPageSize(pageSize);
        Pagination<LogWebVo> pagination = selectList(logQuery,new LogTextQuery());
        return pagination.getDataList();
    }

    /**
     * 测试50秒可以处理500条数据，包括上传到云盘
     * 1w条数据分20次处理，每次500条，需要20分钟
     */
    public void deleteHisData(String month,String model){
        //删除7天前的日志
        long startTime  = System.currentTimeMillis();
        LogQuery logQuery = new LogQuery();
        logQuery.setMonth(month);
        logQuery.setModel(model);
        Date hisDate = DateUtil.getDateAfter(new Date(),-7);
        Long endTime = hisDate.getTime();
        logQuery.setEndTime(DateFormatUtils.format(hisDate,"yyyy-MM-dd HH:mm:ss"));
        int pageSize = 500;
        int yunPanFileSize = 5000;
        int maxCount = 10000;
        long delCount = 0;
        logQuery.setPageSize(pageSize);
        Pagination<LogWebVo> pagination = selectList(logQuery,new LogTextQuery());
//        Queue<String> queue = new ConcurrentLinkedQueue<>();
        List<String> ypDataList = new ArrayList<>();
        //线程池处理删除任务

        final long startMainTime = System.currentTimeMillis();
        ExecutorService ec = new ThreadPoolExecutor(4, 4, 0, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>()){

            @Override protected void terminated() {
                super.terminated();
                bisLog.info("finish, take time = " + (System.currentTimeMillis() - startMainTime));
            }
        };

        while (pagination.getTotalCount() > 0 && delCount < maxCount){
            List<LogWebVo> dataList = pagination.getDataList();
            for(final LogWebVo logWebVo : dataList) {

                ec.execute(new Runnable() {

                    @Override public void run() {
                        try {
                            client.prepareDelete(logWebVo.getIndex(),logWebVo.getType(),logWebVo.getId())
                                    .execute().actionGet();
                            consumerCount.incrementAndGet();
                            yunPanLogQueue.offer(JsonUtils.object2Json(logWebVo));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                try{
                     client.prepareDelete(logWebVo.getIndex(),logWebVo.getType(),logWebVo.getId())
                             .execute().actionGet();
                    ypDataList.add(JsonUtils.object2Json(logWebVo));
//                    queue.add();
                    if(ypDataList.size() >= yunPanFileSize){
                        String time = DateUtil.getFmtyMdHmNoSymbol(new Date().getTime());
                        String file = time + ".txt";
                        String ypData = StringUtil.join(ypDataList,"\n");
                        HttpFileUploadUtils.uploadYunPan(file,ypData.getBytes());
                        ypDataList.clear();
                        bisLog.info("upload yunPan back log for fileName="+file);
                    }
                    delCount++;
                }catch (Exception e){
                    log.error("es prepareDelete failed!,message="+e.getMessage());
                }
            }
            pagination = selectList(logQuery,new LogTextQuery());
        }
        bisLog.info("del es totalCount="+delCount);
        long takeTime = System.currentTimeMillis() - startTime;
        bisLog.info("take time="+takeTime);
    }
}
