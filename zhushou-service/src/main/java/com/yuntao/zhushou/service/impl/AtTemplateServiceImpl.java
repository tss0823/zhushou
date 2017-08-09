package com.yuntao.zhushou.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuntao.zhushou.common.constant.MsgConstant;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.CollectUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.web.MsgResponseObject;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.AtTemplateMapper;
import com.yuntao.zhushou.model.domain.*;
import com.yuntao.zhushou.model.enums.AtParameterRuleType;
import com.yuntao.zhushou.model.query.AtActiveQuery;
import com.yuntao.zhushou.model.query.AtParameterQuery;
import com.yuntao.zhushou.model.query.AtTemplateQuery;
import com.yuntao.zhushou.model.vo.AtActiveVo;
import com.yuntao.zhushou.model.vo.AtParameterVo;
import com.yuntao.zhushou.model.vo.AtTemplateVo;
import com.yuntao.zhushou.model.vo.LogWebVo;
import com.yuntao.zhushou.service.inter.*;
import com.yuntao.zhushou.service.support.deploy.DZMessageHelperServer;
import org.apache.commons.collections4.CollectionUtils;
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
//            active.setName(logWebVo.getUrl());
            active.setUrl(logWebVo.getUrl());

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
                parameter.setDataValue(paramJsonObj.getString(key).getBytes());
                parameter.setActiveId(active.getId());
                parameter.setRuleType(AtParameterRuleType.statics.getCode());
//                parameter.setScript();
                atParameterService.insert(parameter);
            }
        }
    }

    @Override
    public AtTemplateVo getTemplateVo(Long templteId) {
        //
        AtTemplate template = findById(templteId);
        AtTemplateVo templateVo = BeanUtils.beanCopy(template,AtTemplateVo.class);
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
            List<AtParameterVo> parameterVoList = CollectUtils.transList(parameterList,AtParameterVo.class);
            activeVo.setParameterVoList(parameterVoList);
        }
        return templateVo;
    }

    @Override
    @Transactional
    public void start(Long id,User user) {
        //get active list
        AtTemplateVo templateVo = this.getTemplateVo(id);
        List<AtActiveVo> activeVoList = templateVo.getActiveVoList();
        if (CollectionUtils.isEmpty(activeVoList)) {
            return;
        }
        //build RequestRes list
        ResponseRes lastResponseRes = null;
        List<ResponseRes> responseResList = new ArrayList<>();
//        List<RequestRes> requestResList = new ArrayList<>();
        for (AtActiveVo activeVo : activeVoList) {   //每一个 http action
            RequestRes requestRes = new RequestRes();
//            requestResList.add(requestRes);

            //headers
            String headerRow = activeVo.getHeaderRow();
            Map<String, String> headers = JsonUtils.json2Object(headerRow, HashMap.class);
            //处理cooke 防止 重复提交
//            String cookie = headers.get("cookie");
//            StringBuilder sb = new StringBuilder();
//            if (StringUtils.isNotEmpty(cookie)) {
//                StringTokenizer st = new StringTokenizer(cookie, ";");
//                while (st.hasMoreElements()) {
//                    String ele = st.nextElement().toString().trim();
//                    if (ele.startsWith("_access_control_id")) {
//                        continue;
//                    }
//                    if (ele.startsWith("_access_control_val")) {
//                        continue;
//                    }
//                    sb.append(ele);
//                    sb.append(";");
//                }
//            }
//            if (!sb.toString().equals("")) {
//                sb.delete(sb.length() - 1, sb.length());
//            }
//            headers.put("cookie", sb.toString());
            requestRes.setHeaders(headers);
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
                    Integer ruleType = parameterVo.getRuleType();
                    String script = parameterVo.getScript();
                    if (ruleType == AtParameterRuleType.statics.getCode()) {  //静态
                        byte[] dataValue = parameterVo.getDataValue();
//                        String[] strings = script.split("-");
//                        Integer startNum = Integer.valueOf(strings[0].trim());
//                        Integer endNum = Integer.valueOf(strings[1].trim());
//                        Integer realValue = RandomUtils.nextInt(startNum, endNum);
                        value = dataValue.toString();
                    }else if (ruleType == AtParameterRuleType.integer.getCode()) {  //整数
                        String[] strings = script.split("-");
                        Integer startNum = Integer.valueOf(strings[0].trim());
                        Integer endNum = Integer.valueOf(strings[1].trim());
                        Integer realValue = RandomUtils.nextInt(startNum, endNum);
                        value = realValue.toString();
                    } else if (ruleType == AtParameterRuleType.decimal.getCode()) { //小数
                        String[] strings = script.split("-");
                        Double startNum = Double.valueOf(strings[0].trim());
                        Double endNum = Double.valueOf(strings[1].trim());
                        Double realValue = RandomUtils.nextDouble(startNum, endNum);
                        value = realValue.toString();

                    } else if (ruleType == AtParameterRuleType.result.getCode()) { //结果
                        if (lastResponseRes == null) {
                            throw new BizException("lastResponseRes  is null,parameterId=" + parameterVo.getId());
                        }
                        String lastResult = new String(lastResponseRes.getResult());
                        JSONObject resultJsonObj = JSON.parseObject(lastResult);
                        //example data.records[0].id
                        //获取上一个结果的中的值，遍历每一个属性，然后取值
                        String[] strings = script.split("\\.");
                        for (int i = 0; i < strings.length; i++) {
                            String propKey = strings[i];
                            if (i == strings.length - 1) {
                                value = resultJsonObj.getString(propKey);
                                break;
                            } else {
                                int startIndex = propKey.indexOf("[");
                                if (startIndex != -1) {
                                    String newPropKey = propKey.substring(0, startIndex);
                                    int endIndex = propKey.indexOf("]");
                                    String posProp = propKey.substring(startIndex + 1, endIndex);
                                    JSONArray jsonArray = resultJsonObj.getJSONArray(newPropKey);
                                    int propIndex = StringUtils.equals(posProp, "first") ? 0 : jsonArray.size() - 1;
                                    resultJsonObj = jsonArray.getJSONObject(propIndex);
                                } else {
                                    resultJsonObj = resultJsonObj.getJSONObject(propKey);
                                }
                            }
                        }
                    } else if (ruleType == AtParameterRuleType.inter.getCode()) { //接口，主要是RPC 接口取值，后续再考虑 TODO

                    }
                    //set param map
                    paramMap.put(code, value);
                }
            }
            // end params

            //execute http request
            lastResponseRes = HttpNewUtils.execute(requestRes);
            //执行结束，推送消息
            Long companyId = user.getCompanyId();
            Company company = companyService.findById(companyId);
            String sendMsg = "url="+requestRes.getUrl()+",status="+lastResponseRes.getStatus();
            MsgResponseObject msgResponseObject = new MsgResponseObject();
            msgResponseObject.setType(MsgConstant.ReqResType.CORE);
            msgResponseObject.setKey(company.getKey());
            msgResponseObject.setCode(MsgConstant.ResponseCode.NORMAL);
            msgResponseObject.setBizType(MsgConstant.ResponseBizType.TEST_HTTP_EXCUTE);
            msgResponseObject.setData(sendMsg);
            dzMessageHelperServer.offerSendMsg(msgResponseObject);

        }
    }

}
