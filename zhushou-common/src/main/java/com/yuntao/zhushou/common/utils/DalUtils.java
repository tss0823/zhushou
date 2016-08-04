package com.yuntao.zhushou.common.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;

import java.util.Date;

/**
 * Created by shengshan.tang on 2015/12/22 at 16:01
 */
public class DalUtils {

    protected final static Logger bisLog = org.slf4j.LoggerFactory
            .getLogger("bis");

    public static String getTableProfix() {
        return com.yuntao.zhushou.common.utils.DateUtil.getFmt(new Date().getTime(), "yyMMdd");
    }

    public static String getYesterdayTableProfix() {
        Date yesterday = DateUtils.addDays(
                new Date(), -1);
        return com.yuntao.zhushou.common.utils.DateUtil.getFmt(yesterday.getTime(), "yyMMdd");
    }

    public static String getNewTableProfix() {
        Date nowDay = new Date();
        bisLog.info("nowDay=" + nowDay);
        Date tomorrowDay = DateUtils.addDays(
                new Date(), 1);
        bisLog.info("tomorrowDay=" + tomorrowDay);
        String formatDay = DateUtil.getFmt(tomorrowDay.getTime(), "yyMMdd");
        bisLog.info("tomorrowFormatDay=" + formatDay);
        return formatDay;
    }
}
