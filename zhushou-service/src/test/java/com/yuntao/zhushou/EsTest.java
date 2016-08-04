package com.yuntao.zhushou;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.model.vo.LogVo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by shan on 2016/5/3.
 */
public class EsTest {

    public static void main(String [] args){
        try{

            Settings settings = Settings.settingsBuilder()
                    .put("cluster.name", "ec_cluster").build();
//            Client client = TransportClient.builder().settings(settings).build();
//Add transport addresses and do something with the client...
            InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(InetAddress.getByName("114.55.40.74"),9300);
            Client client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(inetSocketTransportAddress);
            System.out.printf("client="+client);

            SearchResponse response = client.prepareSearch("stack_log")
                    .setTypes("_type", "test")
//                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(QueryBuilders.termQuery("mobile", "15267164682"))                 // Query
//                    .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
                    .setFrom(0).setSize(60).setExplain(true)
                    .execute()
                    .actionGet();

            SearchHits hits = response.getHits();
            List<LogVo> dataList = new ArrayList<>();
            if(hits != null){
                long totalCount = hits.getTotalHits();
                if(totalCount > 0){
                    SearchHit searchHits [] =  hits.getHits();
                    for(SearchHit searchHit : searchHits){
                        LogVo hbLogBean = new LogVo();
                        Map<String,Object> beanMap = BeanUtils.beanToMap(hbLogBean);
                        Set<String> keys = beanMap.keySet();
                        for(String key : keys){
                            Object value = searchHit.getSource().get(key);
                            beanMap.put(key,value);
                        }
                        //copy to bean
                        BeanUtils.mapToBean(beanMap,hbLogBean);
                        dataList.add(hbLogBean);
                    }
                }
            }


            System.out.printf("dataList="+dataList);

        }catch (Exception e){
           e.printStackTrace();
        }
    }
}
