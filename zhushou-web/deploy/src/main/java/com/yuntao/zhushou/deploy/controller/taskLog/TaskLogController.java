package com.yuntao.zhushou.deploy.controller.taskLog;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.enums.LogMesssageType;
import com.yuntao.zhushou.model.query.TaskLogQuery;
import com.yuntao.zhushou.model.vo.LogMessageVo;
import com.yuntao.zhushou.model.vo.TaskLogVo;
import com.yuntao.zhushou.service.inter.CompanyService;
import com.yuntao.zhushou.service.inter.TaskLogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("taskLog")
public class TaskLogController extends BaseController {


    @Autowired
    private TaskLogService taskLogService;

    @Autowired
    private CompanyService companyService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject dataList(TaskLogQuery query) {
        User user = userService.getCurrentUser();
        Company company = companyService.findById(user.getCompanyId());
        query.setKey(company.getKey());
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();

        if (StringUtils.isEmpty(query.getModel())) {
            query.setModel("test");
        }
        Pagination<TaskLogVo> pagination = taskLogService.selectByPage(query);
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("selectListByBatchNo")
    @NeedLogin
    public ResponseObject selectListByBatchNo(TaskLogQuery query) {
        query.setPageSize(500); //一次500条记录
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        Pagination<TaskLogVo> pagination = taskLogService.selectListByBatchNo(query);
        List<TaskLogVo> dataList = pagination.getDataList();
        Collections.reverse(dataList); //反转
//        List<String> sqlList = new ArrayList<>();
        List<LogMessageVo> logList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (TaskLogVo logVo : dataList) {
                String message = logVo.getMessage();
                LogMessageVo logMessageVo = new LogMessageVo();
                try {
                    if (logVo.isMaster()) {
//                        continue;
//                        logMessageVo.setKey(logVo.getId());
//                        message = JsonUtils.object2Json(logVo);
                        logMessageVo.setType(LogMesssageType.master.getCode());
                        logMessageVo.setDataMsg(logVo.getDesc());
                    } else {
                        int keyIndex = message.indexOf("^|^");
                        if (keyIndex > -1) {
                            String key = message.substring(0, keyIndex);
                            logMessageVo.setKey(key);
                            message = message.substring(keyIndex + 3);
                        }

                        if (message.startsWith("^SQL^")) {
                            logMessageVo.setType(LogMesssageType.sql.getCode());
                            int index = message.lastIndexOf("^#^");
                            String dataMsg = message.substring(index + 3);
                            logMessageVo.setDataMsg(dataMsg);
                            index = message.indexOf("^#^");
                            index = message.indexOf("^#^", index + 1);
                            int endIndex = message.indexOf("^#^", index + 1);
                            String sql = message.substring(index + 3, endIndex);
                            logMessageVo.setSql(sql);
                            message = message.substring(0, index);
                        } else if (message.startsWith("^CACHE^")) {
                            logMessageVo.setType(LogMesssageType.cache.getCode());
                            int index = message.indexOf(",key=") + 5;
                            int endIndex = message.indexOf(",", index);
                            String key = message.substring(index, endIndex);
                            logMessageVo.setSql(key);

                            index = message.indexOf("^#^");
                            String dataMsg = message.substring(index + 3);
                            logMessageVo.setDataMsg(dataMsg);

                            message = message.substring(0, index);

                        } else {
                            logMessageVo.setType(LogMesssageType.other.getCode());
                            logMessageVo.setDataMsg(logVo.getDesc());
                        }
                    }
                } catch (Exception e) {
                    log.error("parse all log failed", e);
                }
                logMessageVo.setId(logVo.getId());
                logMessageVo.setMessage(message);
                logMessageVo.setTime(logVo.getTime());
                logMessageVo.setLastTime(logVo.getLastTime());
                logList.add(logMessageVo);
            }
        }

        Pagination<LogMessageVo> newPagination = new Pagination<>(pagination.getTotalCount(), pagination.getPageSize(), (int)pagination.getPageNum(), logList);
        responseObject.setData(newPagination);
        return responseObject;

    }
}