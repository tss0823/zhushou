package com.yuntao.zhushou.deploy.controller.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuntao.zhushou.common.utils.*;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.deploy.controller.BaseController;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.LogQuery;
import com.yuntao.zhushou.model.query.LogTextQuery;
import com.yuntao.zhushou.model.vo.LogSqlVo;
import com.yuntao.zhushou.model.vo.LogVo;
import com.yuntao.zhushou.model.vo.LogWebVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.service.inter.CompanyService;
import com.yuntao.zhushou.service.inter.LogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    public ResponseObject selectListByStackId(@RequestParam String month,@RequestParam String model, @RequestParam String stackId) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<LogWebVo> dataList = logService.selectListByStackId(month,model, stackId);
        StringBuilder sb = new StringBuilder();
        Collections.reverse(dataList); //反转
        List<String> sqlList = new ArrayList<>();
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
        responseObject.put("logText", sb.toString());
        return responseObject;
    }

    @RequestMapping("selectFormatListByStackId")
    @NeedLogin
    public ResponseObject selectFormatListByStackId(@RequestParam String month,@RequestParam String model, @RequestParam String stackId) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        List<LogWebVo> dataList = logService.selectListByStackId(month,model, stackId);
        StringBuilder sb = new StringBuilder();
        Collections.reverse(dataList); //反转
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (LogVo logVo : dataList) {
                String message = logVo.getMessage();
                if (StringUtils.startsWith(message, "==>") || StringUtils.startsWith(message, "<==")) {
                    sqlList.add(message);
                }
            }
        }
        //
        if (sqlList.size() > 0) {
            String sqlPre = "Preparing:";
            String sqlPar = "Parameters:";
            LogSqlVo logSqlVo = null;
            boolean isStart = false;
            for (String sql : sqlList) {
                if (sql.startsWith("==>")) {
                    if (!isStart) {
                        isStart = true;
                        logSqlVo = new LogSqlVo();
                    }
                    int startIndex = sql.indexOf(sqlPre);
                    if (startIndex > 0) {  //preparing
                        startIndex = startIndex + sqlPre.length();
                        logSqlVo.setPreSql(sql.substring(startIndex).trim());
                    } else { //parameter
                        startIndex = sql.indexOf(sqlPar) + sqlPar.length();
                        String params = sql.substring(startIndex).trim();
                        if (StringUtils.isEmpty(params)) {
                            continue;
                        }
                        List<String> paramList = new ArrayList<String>();
                        StringTokenizer st = new StringTokenizer(params, "),");
                        while (st.hasMoreElements()) {
                            String param = st.nextElement().toString().trim();
                            if (StringUtils.isNotEmpty(param) && !StringUtils.equals(param, "null")) {
                                paramList.add(param + ")");
                            }
                        }

                    }
                }
            }

        }
        responseObject.put("logText", sb.toString());
        return responseObject;
    }

    @RequestMapping("findMasterByStackId")
    @NeedLogin
    public ResponseObject findMasterByStackId(@RequestParam String month,@RequestParam String model, @RequestParam String stackId) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        LogVo logVo = logService.findMasterByStackId(month,model, stackId);
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
    public ResponseObject httpExecute(@RequestParam String month,@RequestParam String model,@RequestParam String stackId, String reqCookie) {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        LogVo logVo = logService.findMasterByStackId(month,model, stackId);

        String url = logVo.getReqUrl();
        String reqHeads = logVo.getReqHeaders();
        Map<String, String> headers = JsonUtils.json2Object(reqHeads,HashMap.class);
        String parameters = logVo.getParameters();
        Map<String, String> params = JsonUtils.json2Object(parameters,HashMap.class);
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
