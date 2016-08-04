package com.yuntao.zhushou.service.task;

import com.yuntao.zhushou.common.utils.DateUtil;
import com.yuntao.zhushou.service.inter.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by shan on 2016/2/23.
 */
//@Component
public class EsLogJob {

    private final static Logger log = LoggerFactory.getLogger(EsLogJob.class);

    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    @Autowired
    private LogService logService;

//    @Scheduled(cron = "5 1 02 * * ?")
//    @Scheduled(cron = "5 */30 * * * ?")
//    @Scheduled(cron = "5 18 1 * * ?")
    public void deleteHisDataTask() {
        //prod
        bisLog.info("delete es his data prod task start!");
        String month =  DateUtil.getFmt(new Date().getTime(),"yyyy.MM");
        logService.deleteHisData(month,"prod");
        bisLog.info("delete es his data prod task end!");

        //test
        bisLog.info("delete es his data test task start!");
        logService.deleteHisData(month,"test");
        bisLog.info("delete es his data test task end!");
    }

}
