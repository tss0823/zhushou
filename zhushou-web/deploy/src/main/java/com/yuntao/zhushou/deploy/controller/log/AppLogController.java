package com.yuntao.zhushou.deploy.controller.log;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.HttpUtil;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.enums.LogMesssageType;
import com.yuntao.zhushou.model.query.LogQuery;
import com.yuntao.zhushou.model.query.LogTextQuery;
import com.yuntao.zhushou.model.vo.*;
import com.yuntao.zhushou.service.inter.CompanyService;
import com.yuntao.zhushou.service.inter.LogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("appLog")
public class AppLogController extends BaseController {


    @Autowired
    private LogService logService;

    @Autowired
    private CompanyService companyService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject dataList(LogQuery query, LogTextQuery logTextQuery) {
        User user = userService.getCurrentUser();
        Company company = companyService.findById(user.getCompanyId());
        query.setKey(company.getKey());
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
        Pagination<LogWebVo> pagination = logService.selectByPage(query, logTextQuery);
        responseObject.setData(pagination);
        return responseObject;
    }

    @RequestMapping("selectListByStackId")
    @NeedLogin
    public ResponseObject selectListByStackId(@RequestParam String month, @RequestParam String model, @RequestParam String stackId) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<LogWebVo> dataList = logService.selectListByStackId(month, model, stackId);
//        StringBuilder sb = new StringBuilder();
        Collections.reverse(dataList); //反转
//        List<String> sqlList = new ArrayList<>();
        List<LogMessageVo> logList = new ArrayList<>();
        ProfileTaskVo profileTaskVo = null;
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (LogVo logVo : dataList) {
                String message = logVo.getMessage();
                LogMessageVo logMessageVo = new LogMessageVo();
                try {
                    if (logVo.isMaster()) {
                        continue;
//                        logMessageVo.setKey(logVo.getId());
//                        message = JsonUtils.object2Json(logVo);
//                        logMessageVo.setType(LogMesssageType.master.getCode());
                    } else {
                        int keyIndex = message.indexOf("^|^");
                        if (keyIndex > -1) {
                            String key = message.substring(0, keyIndex);
                            logMessageVo.setKey(key);
                            message = message.substring(keyIndex + 3);
                            if (StringUtils.equalsIgnoreCase(key, "methodStack")) {
                                profileTaskVo = JsonUtils.json2Object(message, ProfileTaskVo.class);
                            }
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
                        }
                    }
                } catch (Exception e) {
                    log.error("parse all log failed", e);
                }
                logMessageVo.setMessage(message);
                logList.add(logMessageVo);
            }
        }
//        //
        //聚合成树
        LogMsgTreeVo logMsgTreeVo = transLogTreeVo(0,profileTaskVo,logList);
        responseObject.put("logMsgTreeVo", logMsgTreeVo);
        return responseObject;
    }

    private LogMsgTreeVo transLogTreeVo(Integer level,ProfileTaskVo taskVo, List<LogMessageVo> logList) {
        LogMsgTreeVo logMsgTreeVo = new LogMsgTreeVo();
        logMsgTreeVo.setLevel(level);
        logMsgTreeVo.setName(taskVo.getName() + "," + taskVo.getContent() + ",[" + taskVo.getLevel() + "] take time " + taskVo.getTime() + " ms");
        List<ProfileTaskVo> taskChildList = taskVo.getChildList();
        //add log item
        for (LogMessageVo logMessageVo : logList) {
            String key = logMessageVo.getKey();
            if (StringUtils.equals(key, taskVo.getKey())) {
                logMsgTreeVo.getLogMessageVoList().add(logMessageVo);
            }
        }

        //child
        if (CollectionUtils.isNotEmpty(taskChildList)) {
            level++;
            for (ProfileTaskVo childTaskVo : taskChildList) {
                LogMsgTreeVo childLogMsgTreeVo = transLogTreeVo(level,childTaskVo, logList);
                logMsgTreeVo.getChild().add(childLogMsgTreeVo);
            }
        }
        return logMsgTreeVo;


    }



    @RequestMapping("selectAllListByStackId")
    @NeedLogin
    public ResponseObject selectAllListByStackId(@RequestParam String month,@RequestParam String model, @RequestParam String stackId) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<LogWebVo> dataList = logService.selectListByStackId(month,model, stackId);
        StringBuilder sb = new StringBuilder();
        Collections.reverse(dataList); //反转
//        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (LogVo logVo : dataList) {
                String message = logVo.getMessage();
                if (logVo.isMaster()) {
                    message = JsonUtils.object2Json(logVo);
                }
                sb.append(message + "\r\n");
            }
        }
        //
        responseObject.setData(sb.toString());
        return responseObject;
    }


    @RequestMapping("findMasterByStackId")
    @NeedLogin
    public ResponseObject findMasterByStackId(@RequestParam String month, @RequestParam String model, @RequestParam String stackId) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        LogVo logVo = logService.findMasterByStackId(month, model, stackId);
        LogWebVo logWebVo = BeanUtils.beanCopy(logVo, LogWebVo.class);
//        String response = logWebVo.getResponse();

////        json 格式化
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            Object json = mapper.readValue(response, Object.class);
//            response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//            logWebVo.setResFormatMsg(response);
//        } catch (IOException e) {
//        }
////        end
        responseObject.setData(logWebVo);
        return responseObject;
    }

    @RequestMapping("httpExecute")
    @NeedLogin
    public ResponseObject httpExecute(@RequestParam String month, @RequestParam String model, @RequestParam String stackId, String reqCookie) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        LogVo logVo = logService.findMasterByStackId(month, model, stackId);

        String url = logVo.getReqUrl();
        String reqHeads = logVo.getReqHeaders();
        Map<String, String> headers = JsonUtils.json2Object(reqHeads, HashMap.class);
        String parameters = logVo.getParameters();
        Map<String, String> params = JsonUtils.json2Object(parameters, HashMap.class);
        //处理cooke 防止 重复提交
        String cookie = null;
        if (StringUtils.isNotEmpty(reqCookie)) {
            cookie = reqCookie;
        } else {
            cookie = headers.get("cookie");
        }
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(cookie)) {
            StringTokenizer st = new StringTokenizer(cookie, ";");
            while (st.hasMoreElements()) {
                String ele = st.nextElement().toString().trim();
                if (ele.startsWith("_access_control_id")) {
                    continue;
                }
                if (ele.startsWith("_access_control_val")) {
                    continue;
                }
                sb.append(ele);
                sb.append(";");
            }
        }
        if (!sb.toString().equals("")) {
            sb.delete(sb.length() - 1, sb.length());
        }
        headers.put("cookie", sb.toString());
//        headers.put("cookie", "sid=a6586d73-564d-400a-a41f-2c0906ab2d14");
        try {
            boolean result = HttpUtil.request(url, headers, params);
            responseObject.setData(result);
        } catch (Exception e) {
            responseObject.setSuccess(false);
            responseObject.setMessage(ExceptionUtils.getStackTrace(e));
        }
        return responseObject;
    }
}
