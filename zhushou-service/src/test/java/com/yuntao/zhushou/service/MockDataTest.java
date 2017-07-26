package com.yuntao.zhushou.service;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.IdocUrl;
import com.yuntao.zhushou.model.domain.ProxyReqFilterItem;
import com.yuntao.zhushou.model.domain.ProxyResRewrite;
import com.yuntao.zhushou.model.enums.*;
import com.yuntao.zhushou.model.query.IdocUrlQuery;
import com.yuntao.zhushou.model.vo.IdocUrlVo;
import com.yuntao.zhushou.service.inter.IdocUrlService;
import com.yuntao.zhushou.service.inter.ProxyResRewriteService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.ArrayList;
import java.util.List;

@TransactionConfiguration(defaultRollback = false)
public class MockDataTest extends BaseServiceTest {

    @Autowired
    private IdocUrlService idocUrlService;

    @Autowired
    private ProxyResRewriteService proxyResRewriteService;



//    @Test
    public void test1() {
        IdocUrlQuery query = new IdocUrlQuery();
        Long userId = 1L;
//        Long userId = 8L;
        query.setUserId(userId);
        query.setType(IdocUrlType.inters.getCode());
        query.setPageSize(2000);
        Pagination<IdocUrlVo> pagination = idocUrlService.selectPage(query);
        List<IdocUrlVo> dataList = pagination.getDataList();
        boolean first = true;
        for (IdocUrlVo idocUrlVo : dataList) {
//            if(first){
//                first = false;
//                continue;
//            }
            ProxyResRewrite proxyResRewrite = new ProxyResRewrite();
            proxyResRewrite.setUserId(userId);
            proxyResRewrite.setStatus(YesNoIntType.no.getCode());

            String resultData = idocUrlVo.getResultData();
            String mockData = resultData.replaceAll("\\^#\\^[^\"]*", "");
            mockData = mockData.replaceAll("\\[[^\\]]+\\]", "");
            String compressData = JsonUtils.compress(mockData);
            proxyResRewrite.setData(compressData);
            proxyResRewrite.setJoinType(ProxyFilterJoinType.and.getCode());
            proxyResRewrite.setName(idocUrlVo.getName());
            proxyResRewrite.setPort(8888);
            proxyResRewrite.setResModel(ProxyResRewriteResModel.statics.getCode());
            proxyResRewrite.setResType(ProxyResRewriteResType.body.getCode());

            List<ProxyReqFilterItem> itemList = new ArrayList<>();
            ProxyReqFilterItem proxyReqFilterItem = new ProxyReqFilterItem();
            itemList.add(proxyReqFilterItem);
            proxyReqFilterItem.setMatchType(ProxyMatchType.eq.getCode());
            proxyReqFilterItem.setKey("pathUrl");
            proxyReqFilterItem.setValue(idocUrlVo.getUrl());
            proxyReqFilterItem.setType(ProxyReqFilterType.url.getCode());
            Long mockDataId = proxyResRewriteService.addRewrite(proxyResRewrite, itemList);

            //update set mockDataId
            IdocUrl idocUrl = BeanUtils.beanCopy(idocUrlVo, IdocUrl.class);
            idocUrl.setMockDataId(mockDataId);
            idocUrlService.updateById(idocUrl);
//            break;

        }

    }

    @Test
    public void test2() {
        IdocUrlQuery query = new IdocUrlQuery();
        Long userId = 1L;
//        Long userId = 8L;
        query.setUserId(userId);
        query.setType(IdocUrlType.inters.getCode());
        query.setPageSize(2000);
        Pagination<IdocUrlVo> pagination = idocUrlService.selectPage(query);
        List<IdocUrlVo> dataList = pagination.getDataList();
        boolean first = true;
        for (IdocUrlVo idocUrlVo : dataList) {
//            if(first){
//                first = false;
//                continue;
//            }
            String resultData = idocUrlVo.getResultData();
            String mockData = resultData.replaceAll("\\^#\\^[^\"]*", "");
            mockData = mockData.replaceAll("\\[[^\\]]+\\]", "");
            String compressData = JsonUtils.compress(mockData);

            //update set mockDataId
            IdocUrl idocUrl = BeanUtils.beanCopy(idocUrlVo, IdocUrl.class);
            idocUrl.setResultMockData(compressData);
            idocUrl.setMockStatus(YesNoIntType.no.getCode());

            idocUrlService.updateById(idocUrl);
            break;

        }

    }


    public static void main(String[] args) {
//        String content = "{\"aa\":\"bbb^#^cccc\"}";
        String content = "aa[aaa]bbbb";
        content = content.replaceAll("\\[[^\\]]+\\]", "");
        System.out.println(content);

//        String json = "{\n" +
//                "            \"success\": \"true\",\n" +
//                "            \"level\": \"info\",\n" +
//                "            \"type\": \"normal\",\n" +
//                "            \"bizType\": \"normal\",\n" +
//                "            \"code\": \"00\",\n" +
//                "            \"message\": \"null\",\n" +
//                "            \"data[业务数据]\": {\n" +
//                "                \"money\": \"30000\",\n" +
//                "                \"inviteNum\": \"0\",\n" +
//                "                \"accomplishNum\": \"0\",\n" +
//                "                \"awardMoney\": \"0\"\n" +
//                "            }\n" +
//                "        }\n" +
//                " ";
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            Object obj = mapper.readValue(json, Object.class);
//            String formatData = mapper.writeValueAsString(obj);
//            System.out.println(formatData);
//        } catch (IOException e) {
//        }
    }


}
