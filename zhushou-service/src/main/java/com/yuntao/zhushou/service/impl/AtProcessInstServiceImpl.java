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
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.dal.mapper.AtProcessInstMapper;
import com.yuntao.zhushou.common.constant.AppConstant;
import com.yuntao.zhushou.model.domain.AtProcessInst;
import com.yuntao.zhushou.model.enums.AtParamterRuleType;
import com.yuntao.zhushou.model.query.AtProcessInstQuery;
import com.yuntao.zhushou.model.vo.AtActiveVo;
import com.yuntao.zhushou.model.vo.AtParameterVo;
import com.yuntao.zhushou.model.vo.AtProcessInstVo;
import com.yuntao.zhushou.model.vo.AtTemplateVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.service.inter.AtProcessInstService;
import com.yuntao.zhushou.service.inter.AtTemplateService;
import com.yuntao.zhushou.service.support.YTWebSocketServer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 流程实例服务实现类
 *
 * @author admin
 * @2016-07-21 15
 */
@Service("atProcessInstService")
public class AtProcessInstServiceImpl implements AtProcessInstService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AtProcessInstMapper atProcessInstMapper;

    @Autowired
    private AtTemplateService atTemplateService;

//    @Autowired
    private YTWebSocketServer YTWebSocketServer;

    @Override
    public List<AtProcessInst> selectList(AtProcessInstQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return atProcessInstMapper.selectList(queryMap);
    }

    @Override
    public Pagination<AtProcessInstVo> selectPage(AtProcessInstQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = atProcessInstMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<AtProcessInst> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        queryMap.put("pagination", pagination);
        List<AtProcessInst> dataList = atProcessInstMapper.selectList(queryMap);
        Pagination<AtProcessInstVo> newPageInfo = new Pagination<>(pagination);
        List<AtProcessInstVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for (AtProcessInst atProcessInst : dataList) {
            AtProcessInstVo atProcessInstVo = BeanUtils.beanCopy(atProcessInst, AtProcessInstVo.class);
            newDataList.add(atProcessInstVo);
        }
        return newPageInfo;

    }

    @Override
    public AtProcessInst findById(Long id) {
        return atProcessInstMapper.findById(id);
    }


    @Override
    public int insert(AtProcessInst atProcessInst) {
        return atProcessInstMapper.insert(atProcessInst);
    }

    @Override
    public int updateById(AtProcessInst atProcessInst) {
        return atProcessInstMapper.updateById(atProcessInst);
    }

    @Override
    public int deleteById(Long id) {
        return atProcessInstMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void start(Long id) {
        AtProcessInst processInst = findById(id);
        Long templateId = processInst.getTemplateId();

        //get active list
        AtTemplateVo templateVo = atTemplateService.getTemplateVo(templateId);
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
            String cookie = headers.get("cookie");
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
                    if (ruleType == AtParamterRuleType.integer.getCode()) {  //整数
                        String[] strings = script.split("-");
                        Integer startNum = Integer.valueOf(strings[0].trim());
                        Integer endNum = Integer.valueOf(strings[1].trim());
                        Integer realValue = RandomUtils.nextInt(startNum, endNum);
                        value = realValue.toString();
                    } else if (ruleType == AtParamterRuleType.decimal.getCode()) { //小数
                        String[] strings = script.split("-");
                        Double startNum = Double.valueOf(strings[0].trim());
                        Double endNum = Double.valueOf(strings[1].trim());
                        Double realValue = RandomUtils.nextDouble(startNum, endNum);
                        value = realValue.toString();

                    } else if (ruleType == AtParamterRuleType.result.getCode()) { //结果
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
                    } else if (ruleType == AtParamterRuleType.inter.getCode()) { //接口，主要是RPC 接口取值，后续再考虑 TODO

                    }
                    //set param map
                    paramMap.put(code, value);
                }
            }
            // end params

            //execute http request
            lastResponseRes = HttpNewUtils.execute(requestRes);
            //执行结束，推送消息
            String sendMsg = "url="+requestRes.getUrl()+",status="+lastResponseRes.getStatus();
            YTWebSocketServer.sendMessage(MsgConstant.ResponseBizType.TEST_HTTP_EXCUTE,sendMsg);

        }
    }


    public static void main(String[] args) {
        String[] strings = "data.records[0].id".split("\\.");
        System.out.printf("strings=" + strings);
    }

}
