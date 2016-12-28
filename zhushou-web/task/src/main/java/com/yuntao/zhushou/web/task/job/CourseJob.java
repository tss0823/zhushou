package com.yuntao.zhushou.web.task.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by shan on 2016/2/23.
 */
@Component
public class CourseJob extends  BaseJob {


//    @Autowired
//    private CourseService courseService;

    /**
     * 每天凌晨00:01始执行
     */
    @Scheduled(cron = "0 1 0 *0 * ?")
    public void execute() {
//        courseService.startAndEndTask();
    }



}
