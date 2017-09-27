package com.yuntao.zhushou.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuntao.zhushou.common.constant.MsgConstant;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.*;
import com.yuntao.zhushou.common.web.MsgResponseObject;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.dal.mapper.AtTemplateMapper;
import com.yuntao.zhushou.model.domain.*;
import com.yuntao.zhushou.model.enums.AtActiveCollectType;
import com.yuntao.zhushou.model.enums.AtParameterDataType;
import com.yuntao.zhushou.model.enums.YesNoIntType;
import com.yuntao.zhushou.model.param.DataMap;
import com.yuntao.zhushou.model.query.*;
import com.yuntao.zhushou.model.vo.*;
import com.yuntao.zhushou.service.inter.*;
import com.yuntao.zhushou.service.support.deploy.DZMessageHelperServer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 测试模板服务实现类
 *
 * @author admin
 * @2016-07-21 15
 */
@Service("atTemplateService")
public class AtTemplateServiceImpl implements AtTemplateService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AtTemplateMapper atTemplateMapper;

    @Autowired
    private LogService logService;

    @Autowired
    private AtActiveService atActiveService;

    @Autowired
    private AtParameterService atParameterService;

    @Autowired
    private DZMessageHelperServer dzMessageHelperServer;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AtActiveInstService atActiveInstService;

    @Autowired
    private AtProcessInstService atProcessInstService;

    @Autowired
    private AtVariableService atVariableService;

    @Autowired
    private IdocUrlService idocUrlService;


    @Override
    public List<AtTemplate> selectList(AtTemplateQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return atTemplateMapper.selectList(queryMap);
    }

    @Override
    public Pagination<AtTemplateVo> selectPage(AtTemplateQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = atTemplateMapper.selectListCount(queryMap);
        Pagination<AtTemplate> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        Pagination<AtTemplateVo> newPageInfo = new Pagination<>(pagination);
        if (totalCount == 0) {
            return newPageInfo;
        }
        queryMap.put("pagination", pagination);
        List<AtTemplate> dataList = atTemplateMapper.selectList(queryMap);
        List<AtTemplateVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for (AtTemplate atTemplate : dataList) {
            AtTemplateVo atTemplateVo = BeanUtils.beanCopy(atTemplate, AtTemplateVo.class);
            newDataList.add(atTemplateVo);
        }
        return newPageInfo;

    }

    @Override
    public AtTemplate findById(Long id) {
        return atTemplateMapper.findById(id);
    }


    @Override
    public int insert(AtTemplate atTemplate) {
        return atTemplateMapper.insert(atTemplate);
    }

    @Override
    public int updateById(AtTemplate atTemplate) {
        return atTemplateMapper.updateById(atTemplate);
    }

    @Override
    public int deleteById(Long id) {
        return atTemplateMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void save(AtTemplateVo templateVo, List<String> logIds) {
        String model = templateVo.getModel();
        String month = templateVo.getMonth();
        AtTemplate template = BeanUtils.beanCopy(templateVo, AtTemplate.class);
        this.insert(template);

        //处理活动模板
        for (String logId : logIds) {
            LogWebVo logWebVo = logService.findMasterByStackId(month, model, logId);
            AtActive active = new AtActive();
            active.setName(logWebVo.getUrl());
            active.setUrl(logWebVo.getReqUrl());

            String reqHeaders = logWebVo.getReqHeaders();
            JSONObject headerJsonObj = JSON.parseObject(reqHeaders);

            String conentType = headerJsonObj.getString("Content-Type");
            active.setReqContentType(conentType);

            active.setHeaderRow(logWebVo.getReqHeaders());
            active.setMethod("POST");
            active.setTemplateId(template.getId());
            atActiveService.insert(active);

            //处理参数模板
            String parameters = logWebVo.getParameters();
            JSONObject paramJsonObj = JSON.parseObject(parameters);
            Set<String> keySet = paramJsonObj.keySet();
            for (String key : keySet) {
                AtParameter parameter = new AtParameter();
//                parameter.setName();
                parameter.setCode(key);
//                parameter.setDataType(key);
                //byte 为文件流做准备,之后读取key后，可以修改value
                parameter.setDataValue(paramJsonObj.getString(key));
                parameter.setActiveId(active.getId());
                parameter.setDataType(AtParameterDataType.statics.getCode());
//                parameter.setScript();
                atParameterService.insert(parameter);
            }
        }
    }

    @Override
    public AtTemplateVo getTemplateVo(Long templteId) {
        //
        AtTemplate template = findById(templteId);
        AtTemplateVo templateVo = BeanUtils.beanCopy(template, AtTemplateVo.class);
        //
        AtActiveQuery activeQuery = new AtActiveQuery();
        activeQuery.setTemplateId(templteId);
        activeQuery.setOrderByColumn("orderIndex");
        activeQuery.setAscOrDesc("asc");
        List<AtActive> activeList = atActiveService.selectList(activeQuery);
        List<AtActiveVo> activeVoList = CollectUtils.transList(activeList, AtActiveVo.class);
        if (CollectionUtils.isEmpty(activeVoList)) {
            return templateVo;
        }
        templateVo.setActiveVoList(activeVoList);
        for (AtActiveVo activeVo : activeVoList) {
            //header
            String headerRow = activeVo.getHeaderRow();
            if (StringUtils.isNotEmpty(headerRow)) {
                Map headerMap = JsonUtils.json2Object(headerRow, Map.class);
                Set<Map.Entry<String, String>> headerSet = headerMap.entrySet();
                List<DataMap> headerMapList = new ArrayList<>();
                for (Map.Entry<String, String> entry : headerSet) {
                    DataMap dataMap = new DataMap(entry.getKey(), entry.getValue());
                    headerMapList.add(dataMap);
                }
                activeVo.setDataMapList(headerMapList);
            }
            //
            AtParameterQuery parameterQuery = new AtParameterQuery();
            parameterQuery.setActiveId(activeVo.getId());
            List<AtParameter> parameterList = atParameterService.selectList(parameterQuery);
            if (CollectionUtils.isEmpty(parameterList)) {
                continue;
            }
            List<AtParameterVo> parameterVoList = CollectUtils.transList(parameterList, AtParameterVo.class);
            activeVo.setParameterVoList(parameterVoList);
        }
        return templateVo;
    }

    @Transactional
    @Override
    public void collect(Long id, String companyKey, String model, Integer type, String appName, String mobile, String startTime, String endTime,
                        List<String> logStackIds, Integer orderIndex) {
        List<LogWebVo> dataList = new ArrayList<>();
        if (AtActiveCollectType.ids.getCode() == type) {
            for (String logStackId : logStackIds) {
                LogWebVo logWebVo = logService.findMasterByStackId(null, model, logStackId);
                dataList.add(logWebVo);
            }
        } else {
            LogQuery logQuery = new LogQuery();
            logQuery.setPageSize(1000);
            logQuery.setKey(companyKey);
            logQuery.setModel(model);
            logQuery.setAppName(appName);
            logQuery.setMaster(true);
            logQuery.setStartTime(startTime);
            logQuery.setEndTime(endTime);
            LogTextQuery logTextQuery = new LogTextQuery();
            logTextQuery.setMobile(mobile);
            Pagination<LogWebVo> pagination = logService.selectList(logQuery, logTextQuery);
            dataList = pagination.getDataList();

        }
        List<String> savedKeyList = new ArrayList<>();
        //获取最后一个active
        int lastOrderIndex = 0;
        if (orderIndex != null) {
            lastOrderIndex = orderIndex - 1;
        } else {
            AtActiveQuery activeQuery = new AtActiveQuery();
            activeQuery.setTemplateId(id);
            activeQuery.setOrderByColumn("orderIndex");
            activeQuery.setAscOrDesc("desc");
            activeQuery.setLimit(1);
            List<AtActive> activeList = atActiveService.selectList(activeQuery);
            if (CollectionUtils.isNotEmpty(activeList)) {
                lastOrderIndex = NumberUtil.getNumber(activeList.get(0).getOrderIndex());
            }
        }

        Company company = companyService.findByKey(companyKey);


        for (LogWebVo logWebVo : dataList) {
            String url = logWebVo.getUrl();
            if (!logWebVo.getStatus().toString().startsWith("2") && !logWebVo.getStatus().toString().startsWith("3")) { //2xxx and 3xxx is ok
                continue;
            }
            if (url.endsWith(".png") || url.endsWith(".css") || url.endsWith(".js") || url.contains(".woff") || url.endsWith(".xml")) { //资源文件过滤调
                continue;
            }
            String parameters = logWebVo.getParameters();
            String saveKey = url.trim() + "_" + parameters.trim();
            if (savedKeyList.contains(saveKey)) {  //自动过滤调重复的req
                continue;
            }
            //add
            savedKeyList.add(saveKey);

            //active
            AtActive atActive = new AtActive();
            atActive.setTemplateId(id);
            atActive.setLogStackId(logWebVo.getStackId());
            IdocUrl idocUrl = idocUrlService.findNewsDocByUrl(company.getId(),logWebVo.getAppName(), logWebVo.getUrl());
            if(idocUrl != null){
                atActive.setName(idocUrl.getName());
            }else{
                atActive.setName(logWebVo.getUrl());
            }
            atActive.setUrl(logWebVo.getReqUrl());
            String reqHeaders = logWebVo.getReqHeaders();
            JSONObject headerJsonObj = JSON.parseObject(reqHeaders);
            headerJsonObj.remove("Cookie");   //去掉cookie
//            String conentType = headerJsonObj.getString("Content-Type");
            atActive.setReqContentType("application/x-www-form-urlencoded");
            atActive.setHeaderRow(headerJsonObj.toString());
            atActive.setMethod("POST");
            atActive.setOrderIndex(++lastOrderIndex);
            atActiveService.insert(atActive);

            //处理参数模板
            parameters = logWebVo.getParameters();
            JSONObject paramJsonObj = JSON.parseObject(parameters);
            Set<String> keySet = paramJsonObj.keySet();
            for (String key : keySet) {
                AtParameter parameter = new AtParameter();
                parameter.setName(key);
                parameter.setCode(key);
                parameter.setDataValue(paramJsonObj.getString(key));
                parameter.setActiveId(atActive.getId());
                parameter.setDataType(AtParameterDataType.statics.getCode());
                atParameterService.insert(parameter);
            }
        }

    }

    @Transactional
    public void saveActiveSort(Long templateId, List<Long> activeIds) {
        int orderIndex = 1;
        for (Long activeId : activeIds) {
            AtActive atActive = atActiveService.findById(activeId);
            atActive.setOrderIndex(orderIndex);
            atActiveService.updateById(atActive);
            orderIndex++;


        }

    }

    @Override
    @Transactional
    public void start(Long id, User user) {

        //get active list
        AtTemplateVo templateVo = this.getTemplateVo(id);
        //new processInst
        AtProcessInst atProcessInst = new AtProcessInst();
        atProcessInst.setTemplateId(templateVo.getId());
        atProcessInst.setName(templateVo.getName());
        atProcessInst.setStatus(YesNoIntType.yes.getCode());
        atProcessInst.setUserId(user.getId());
        atProcessInstService.insert(atProcessInst);

        List<AtActiveVo> activeVoList = templateVo.getActiveVoList();
        if (CollectionUtils.isEmpty(activeVoList)) {
            return;
        }

        //获取变量
        Map<String, Object> variableMap = atVariableService.getVariableMap(id);

        Long companyId = user.getCompanyId();
        Company company = companyService.findById(companyId);

        try {
            httpChainExecute(atProcessInst.getId(), company.getKey(), activeVoList, variableMap);
        } catch (Exception e) {
            atProcessInst.setStatus(YesNoIntType.no.getCode());
            atProcessInst.setErrMsg(ExceptionUtils.getPrintStackTrace(e));
            atProcessInstService.updateById(atProcessInst);
        }

    }

    @Override
    public void start2(Long id, User user) {
        //执行结束，推送消息
        Company company = companyService.findById(user.getCompanyId());
        String sendMsg = "test msg !!!";
        MsgResponseObject msgResponseObject = new MsgResponseObject();
        msgResponseObject.setType(MsgConstant.ReqResType.USER);
        msgResponseObject.setKey(company.getKey());
        msgResponseObject.setCode(MsgConstant.ResponseCode.NORMAL);
        msgResponseObject.setBizType(MsgConstant.ResponseBizType.TEST_ACTIVE_HTTP_EXCUTE);
        msgResponseObject.setData(sendMsg);
        dzMessageHelperServer.offerSendMsg(msgResponseObject);
    }

    private void httpChainExecute(Long processInstId, String companyKey, List<AtActiveVo> activeVoList, Map<String, Object> variableMap) {
        ResponseRes lastResponseRes = null;
        Map<String, String> resCookieMap = new HashMap<>();
        for (AtActiveVo activeVo : activeVoList) {   //每一个 http action
            AtActiveInst atActiveInst = new AtActiveInst();
            atActiveInst.setStatus(YesNoIntType.yes.getCode());
            atActiveInst.setActiveId(activeVo.getId());
            RequestRes requestRes = new RequestRes();
            requestRes.setMethod(activeVo.getMethod());
            String reqUrl = activeVo.getUrl();
            reqUrl = TemplateUtils.render(reqUrl, variableMap);  //替换url 有变量的地方
            requestRes.setUrl(reqUrl);
            try {
//            requestResList.add(requestRes);
                //headers
                String headerRow = activeVo.getHeaderRow();
                Map<String, String> headers = JsonUtils.json2Object(headerRow, HashMap.class);
                requestRes.setHeaders(headers);
                atActiveInst.setReqHeader(headerRow);


                //params
                List<AtParameterVo> parameterVoList = activeVo.getParameterVoList();
                if (CollectionUtils.isNotEmpty(parameterVoList)) {
                    Map<String, String> paramMap = new HashMap<>();
                    requestRes.setParams(paramMap);


                    for (AtParameterVo parameterVo : parameterVoList) { //每个http action 请求参数
                        String code = parameterVo.getCode();
                        //TODO file
                        String value = new String(parameterVo.getDataValue());
                        parameterVo.getDataType();
                        Integer dataType = parameterVo.getDataType();
                        String dataValue = parameterVo.getDataValue();
                        if (dataType == AtParameterDataType.statics.getCode()) {  //静态
                            value = dataValue.toString();
                            value = TemplateUtils.render(value, variableMap);

                        } else if (dataType == AtParameterDataType.integer.getCode()) {  //整数
                            String[] strings = dataValue.split(",");
                            Integer startNum = Integer.valueOf(strings[0].trim());
                            Integer endNum = Integer.valueOf(strings[1].trim());
                            Integer realValue = RandomUtils.nextInt(startNum, endNum);
                            value = realValue.toString();
                        } else if (dataType == AtParameterDataType.decimal.getCode()) { //小数
                            String[] strings = dataValue.split(",");
                            Double startNum = Double.valueOf(strings[0].trim());
                            Double endNum = Double.valueOf(strings[1].trim());
                            Double realValue = RandomUtils.nextDouble(startNum, endNum);
                            value = realValue.toString();

                        } else if (dataType == AtParameterDataType.str.getCode()) { //小数
                            String[] strings = dataValue.split(",");
                            String str = strings[0].trim();
                            Integer length = Integer.valueOf(strings[1].trim());
                            String randomStr = RandomStringUtils.random(length, true, true);
                            value = randomStr;

                        } else if (dataType == AtParameterDataType.result.getCode()) { //结果
                            if (lastResponseRes == null) {
                                throw new BizException("lastResponseRes  is null,parameterId=" + parameterVo.getId());
                            }

                            value = dataValue.toString();
                            value = TemplateUtils.render(value, variableMap);
                        } else if (dataType == AtParameterDataType.inter.getCode()) { //接口，主要是RPC 接口取值，后续再考虑 TODO

                        }
                        //set param map
                        paramMap.put(code, value);
                        atActiveInst.setParam(JsonUtils.object2Json(paramMap));
                    }
                }
                //end param

                //cookie set header
                if (headers == null) {
                    headers = new HashMap<>();
                }
                Set<Map.Entry<String, String>> entries = resCookieMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    headers.put(entry.getKey(), entry.getValue());
                }
                requestRes.setHeaders(headers);
                //end

                //execute http request
                lastResponseRes = HttpNewUtils.execute(requestRes);
                if (lastResponseRes.getStatus() != 200) {
                    throw new BizException("活动栈执行失败");
                }

                //cookie store
                Map<String, String> resHeaderMap = lastResponseRes.getHeaders();
                String setCookie = resHeaderMap.get("Set-Cookie");
                if (StringUtils.isNotEmpty(setCookie)) {
                    StringTokenizer st = new StringTokenizer(setCookie);
                    while (st.hasMoreElements()) {
                        String cookieEle = st.nextElement().toString();
                        String[] cookieStrs = cookieEle.split("=");
                        resCookieMap.put(cookieStrs[0], cookieStrs[1]);
                    }
                }
                //end

                String lastResult = new String(lastResponseRes.getResult());
                ResponseObject responseObject = JsonUtils.json2Object(lastResult, ResponseObject.class);
                atActiveInst.setStatus(responseObject.isSuccess() ? YesNoIntType.yes.getCode() : YesNoIntType.no.getCode());
                String url = activeVo.getUrl();
                String urlPath = UrlUtils.buildURL(url).getPath();
                String resObjKey = urlPath.substring(1).replaceAll("/", "_");
                variableMap.put(resObjKey, responseObject);  //每个栈的http返回对象 /user/getRegisterSMSCode > user_getRegisterSMSCode
                variableMap.put("prevResponseObject", responseObject);
                variableMap.put("prevResponseHeader", lastResponseRes.getHeaders());

            } catch (Exception e) {
                atActiveInst.setStatus(YesNoIntType.no.getCode());
                atActiveInst.setErrMsg(ExceptionUtils.getPrintStackTrace(e));
            }

            //存储到db
            if (lastResponseRes != null) {
                atActiveInst.setHttpStatus(lastResponseRes.getStatus());
                atActiveInst.setResult(lastResponseRes.getBodyText());
            }
            atActiveInst.setName(activeVo.getName());
            atActiveInst.setProcessInstId(processInstId);
            atActiveInst.setResHeader(JsonUtils.object2Json(lastResponseRes.getHeaders()));
            atActiveInstService.insert(atActiveInst);

            //执行结束，推送消息
            ActiveExecuteVo activeExecuteVo = new ActiveExecuteVo();
            activeExecuteVo.setActiveId(activeVo.getId());
            activeExecuteVo.setSuccess(atActiveInst.getStatus().intValue() == YesNoIntType.yes.getCode());
            activeExecuteVo.setResult(atActiveInst.getResult());
            activeExecuteVo.setErrMsg(atActiveInst.getErrMsg());
            MsgResponseObject msgResponseObject = new MsgResponseObject();
            msgResponseObject.setType(MsgConstant.ReqResType.USER);
            msgResponseObject.setKey(companyKey);
            msgResponseObject.setCode(MsgConstant.ResponseCode.NORMAL);
            msgResponseObject.setBizType(MsgConstant.ResponseBizType.TEST_ACTIVE_HTTP_EXCUTE);
            msgResponseObject.setData(JsonUtils.object2Json(activeExecuteVo));
            dzMessageHelperServer.offerSendMsg(msgResponseObject);

            if (!activeExecuteVo.isSuccess()) {  //只要其中一个活动栈失败，就终止
                break;
            }
        }
    }

}
