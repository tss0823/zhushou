package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.query.TaskLogQuery;
import com.yuntao.zhushou.model.vo.TaskLogVo;
import com.yuntao.zhushou.common.web.Pagination;

/**
 * Created by shan on 2016/5/5.
 */
public interface TaskLogService {

    Pagination<TaskLogVo> selectByPage(TaskLogQuery taskLogQuery);

    Pagination<TaskLogVo> selectListByBatchNo(TaskLogQuery query);

    TaskLogVo findById(String month, String model, String id);

}
