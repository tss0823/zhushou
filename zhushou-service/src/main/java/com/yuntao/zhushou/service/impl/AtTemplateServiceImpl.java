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
import com.yuntao.zhushou.model.enums.AtParameterDataType;
import com.yuntao.zhushou.model.enums.YesNoIntType;
import com.yuntao.zhushou.model.query.*;
import com.yuntao.zhushou.model.vo.AtActiveVo;
import com.yuntao.zhushou.model.vo.AtParameterVo;
import com.yuntao.zhushou.model.vo.AtTemplateVo;
import com.yuntao.zhushou.model.vo.LogWebVo;
import com.yuntao.zhushou.service.inter.*;
import com.yuntao.zhushou.service.support.deploy.DZMessageHelperServer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
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
        List<AtActive> activeList = atActiveService.selectList(activeQuery);
        List<AtActiveVo> activeVoList = CollectUtils.transList(activeList, AtActiveVo.class);
        if (CollectionUtils.isEmpty(activeVoList)) {
            return templateVo;
        }
        templateVo.setActiveVoList(activeVoList);
        for (AtActiveVo activeVo : activeVoList) {
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
    public void collect(Long id,String companyKey, String model,String appName, String mobile, String startTime, String endTime) {
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

        List<LogWebVo> dataList = pagination.getDataList();
        for (LogWebVo logWebVo : dataList) {
            //active
            AtActive atActive = new AtActive();
            atActive.setTemplateId(id);
            atActive.setName(logWebVo.getUrl());
            atActive.setUrl(logWebVo.getReqUrl());
            String reqHeaders = logWebVo.getReqHeaders();
//            JSONObject headerJsonObj = JSON.parseObject(reqHeaders);
//            String conentType = headerJsonObj.getString("Content-Type");
            atActive.setReqContentType("application/x-www-form-urlencoded");
            atActive.setHeaderRow(logWebVo.getReqHeaders());
            atActive.setMethod("POST");
            atActiveService.insert(atActive);

            //处理参数模板
            String parameters = logWebVo.getParameters();
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
            httpChainExecute(atProcessInst.getId(), company.getKey(),activeVoList, variableMap);
        } catch (Exception e) {
            atProcessInst.setStatus(YesNoIntType.no.getCode());
            atProcessInst.setErrMsg(ExceptionUtils.getPrintStackTrace(e));
            atProcessInstService.updateById(atProcessInst);
        }

    }

    private void httpChainExecute(Long processInstId, String companyKey,List<AtActiveVo> activeVoList,Map<String, Object> variableMap) {
        ResponseRes lastResponseRes = null;
        for (AtActiveVo activeVo : activeVoList) {   //每一个 http action
            AtActiveInst atActiveInst = new AtActiveInst();
            atActiveInst.setStatus(YesNoIntType.yes.getCode());
            atActiveInst.setActiveId(activeVo.getId());
            RequestRes requestRes = new RequestRes();
            requestRes.setMethod(activeVo.getMethod());
            requestRes.setUrl(activeVo.getUrl());
            try {
//            requestResList.add(requestRes);
                //headers
                String headerRow = activeVo.getHeaderRow();
                Map<String, String> headers = JsonUtils.json2Object(headerRow, HashMap.class);
                requestRes.setHeaders(headers);
                atActiveInst.setReqHeader(headerRow);
                //end cookie

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
                            value = TemplateUtils.render(value,variableMap);

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
                            String lastResult = new String(lastResponseRes.getResult());
                            ResponseObject responseObject = JsonUtils.json2Object(lastResult, ResponseObject.class);
                            variableMap.put("responseObject",responseObject);
                            value = dataValue.toString();
                            value = TemplateUtils.render(value,variableMap);
                        } else if (dataType == AtParameterDataType.inter.getCode()) { //接口，主要是RPC 接口取值，后续再考虑 TODO

                        }
                        //set param map
                        paramMap.put(code, value);
                        atActiveInst.setParam(JsonUtils.object2Json(paramMap));
                    }
                }
                //end param

                //execute http request
                lastResponseRes = HttpNewUtils.execute(requestRes);

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
            String sendMsg = "url=" + requestRes.getUrl() + ",status=" + lastResponseRes.getStatus();
            MsgResponseObject msgResponseObject = new MsgResponseObject();
            msgResponseObject.setType(MsgConstant.ReqResType.CORE);
            msgResponseObject.setKey(companyKey);
            msgResponseObject.setCode(MsgConstant.ResponseCode.NORMAL);
            msgResponseObject.setBizType(MsgConstant.ResponseBizType.TEST_HTTP_EXCUTE);
            msgResponseObject.setData(sendMsg);
            dzMessageHelperServer.offerSendMsg(msgResponseObject);
        }
    }

}
