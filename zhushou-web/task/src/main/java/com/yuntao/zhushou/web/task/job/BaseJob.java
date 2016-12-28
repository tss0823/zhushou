package com.yuntao.zhushou.web.task.job;

import com.yuntao.zhushou.service.impl.AbstService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tangshengshan on 16-10-19.
 */
public class BaseJob {
//    private final static Logger stackLog = LoggerFactory.getLogger("stackLog");

    private final static Logger taskLog = LoggerFactory.getLogger("taskLog");

    protected final static Logger log = LoggerFactory.getLogger(AbstService.class);
}
