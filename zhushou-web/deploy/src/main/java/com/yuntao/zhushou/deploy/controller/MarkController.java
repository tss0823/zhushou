package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.query.MarkQuery;
import com.yuntao.zhushou.model.vo.MarkVo;
import com.yuntao.zhushou.model.vo.mark.MarkTopNVo;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("mark")
public class MarkController extends BaseController {


    @Autowired
    private AppService appService;


    @Autowired
    private MarkService markService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(MarkQuery query) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        Integer type = query.getType();
        Integer startPeriods = query.getStartPeriods();
        int endPeriods = query.getEndPeriods();
        Integer topN = query.getTopN();
        List<MarkTopNVo> topMinList = markService.selectTopN(type, startPeriods, endPeriods, "asc",topN);
        List<MarkTopNVo> topMaxList = markService.selectTopN(type, startPeriods, endPeriods, "desc",topN);

        int lastItemIndex = markService.selectLastItemIndex(endPeriods);

        MarkQuery markQuery = new MarkQuery();
        markQuery.setPageSize(49);
        markQuery.setAscOrDesc("desc");
        markQuery.setOrderByColumn("itemIndex");
        Pagination<MarkVo> pagination = markService.selectPage(markQuery);
        List<MarkVo> lastDataList = pagination.getDataList();

        int startNum = 1;
        int endNum = 49;
        Map<Integer,Integer> scoreMap = new TreeMap<>();
        for(int i = startNum; i < endNum; i++){
            int itemIndex = markService.selectLastLocation(endPeriods,i);
            int stepLocation = lastItemIndex - itemIndex;

            int totalScore = 100;

            //local
            int localScore = stepLocation / 7;
            totalScore += localScore;

            //max
            for (int y = 0; y < topMaxList.size(); y++) {
                MarkTopNVo markTopNVo = topMaxList.get(y);
                if(markTopNVo.getVal() == i){
                    totalScore -= (topMaxList.size() - y);
                    break;
                }
            }

            //min
            for (int y = 0; y < topMinList.size(); y++) {
                MarkTopNVo markTopNVo = topMinList.get(y);
                if(markTopNVo.getVal() == i){
                    totalScore += (topMinList.size() - y);
                    break;
                }
            }

            //last Data
            for (int y = 0; y < lastDataList.size(); y++) {
                MarkVo markVo = lastDataList.get(y);
                if(markVo.getVal() == i){
                    totalScore--;
                }
            }

            scoreMap.put(i,totalScore);

        }

        //value 排序
        List<Map.Entry<Integer, Integer>> valueList = new ArrayList<>(scoreMap.entrySet());
        Collections.sort(valueList, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        StringBuilder sb = new StringBuilder();
        int maxSize = 7;
        for (Map.Entry<Integer, Integer> entry : valueList) {
            maxSize--;
            sb.append(entry.getKey());
            sb.append(",");
            if(maxSize == 0){
                break;
            }
        }
        responseObject.setData(sb.toString());
        return responseObject;
    }



}
