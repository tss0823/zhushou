package com.yuntao.zhushou.deploy.controller.taskLog;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.query.TaskLogQuery;
import com.yuntao.zhushou.model.vo.TaskLogVo;
import com.yuntao.zhushou.model.web.Pagination;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.TaskLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("taskLog")
public class TaskLogController extends BaseController {


    @Autowired
    private TaskLogService taskLogService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject dataList(TaskLogQuery query) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();

//        String cookieKey = AppConstant.AppLog.APP_LOG_SHOW_ALL;
//        //把条件放到cookie中
//        if (query.getShowAll() != null) {
//            WebContextUtils.setCookie(cookieKey,query.getShowAll().toString(), DateUtil.MONTH_SECONDS);
//        }
//        //end

        if (StringUtils.isEmpty(query.getModel())) {
            query.setModel("test");
        }
        Pagination<TaskLogVo> pagination = taskLogService.selectByPage(query);
        responseObject.setData(pagination);
        return responseObject;
    }

//    @RequestMapping("selectListByBatchNo")
//    @NeedLogin
//    public ResponseObject selectListByBatchNo(@RequestParam String month,@RequestParam String model, @RequestParam String batchNo) {
//        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
//        List<TaskLogVo> dataList = taskLogService.selectListByBatchNo(month, model, batchNo);
//        StringBuilder sb = new StringBuilder();
//        Collections.reverse(dataList); //反转
//        List<String> sqlList = new ArrayList<>();
//        if (CollectionUtils.isNotEmpty(dataList)) {
//            for (LogVo logVo : dataList) {
//                String message = logVo.getMessage();
//                if (logVo.isMaster()) {
//                    message = JsonUtils.object2Json(logVo);
//                }
//                sb.append(message + "\r\n");
//            }
//        }
//        //
//        responseObject.put("logText", sb.toString());
//        return responseObject;
//    }


}
