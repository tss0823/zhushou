package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.query.LogQuery;
import com.yuntao.zhushou.model.query.LogTextQuery;
import com.yuntao.zhushou.model.vo.LogVo;
import com.yuntao.zhushou.model.vo.LogWebVo;
import com.yuntao.zhushou.model.web.Pagination;

import java.util.List;

/**
 * Created by shan on 2016/5/5.
 */
public interface LogService {

    Pagination<LogWebVo> selectByPage(LogQuery query, LogTextQuery logTextQuery);

    List<LogWebVo> selectListByStackId(String month, String model, String stackId);

    LogWebVo findMasterByStackId(String month, String model, String stackId);

    Pagination<LogWebVo> selectList(LogQuery query, LogTextQuery logTextQuery);

    void delHisDataTask();

    void deleteHisData(String month, String model);
}
