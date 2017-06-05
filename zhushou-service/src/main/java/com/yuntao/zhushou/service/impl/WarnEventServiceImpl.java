/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.constant.AppConstant;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.utils.SendEmailUtils;
import com.yuntao.zhushou.common.utils.SendMsgUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.WarnEventMapper;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.domain.WarnEvent;
import com.yuntao.zhushou.model.domain.WarnEventResult;
import com.yuntao.zhushou.model.enums.WarnEventResultType;
import com.yuntao.zhushou.model.enums.YesNoIntType;
import com.yuntao.zhushou.model.query.WarnEventQuery;
import com.yuntao.zhushou.model.vo.WarnEventVo;
import com.yuntao.zhushou.service.inter.UserService;
import com.yuntao.zhushou.service.inter.WarnEventResultService;
import com.yuntao.zhushou.service.inter.WarnEventService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("warnEventService")
public class WarnEventServiceImpl extends AbstService implements WarnEventService {


    @Autowired
    private WarnEventMapper warnEventMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private WarnEventResultService warnEventResultService;

    @Override
    public List<WarnEvent> selectList(WarnEventQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return warnEventMapper.selectList(queryMap);
    }

    @Override
    public WarnEvent selectOne(WarnEventQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<WarnEvent> warnEvents = warnEventMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(warnEvents)) {
            return warnEvents.get(0);
        }
        return null;
    }

    @Override
    public Pagination<WarnEventVo> selectPage(WarnEventQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = warnEventMapper.selectListCount(queryMap);
        Pagination<WarnEventVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<WarnEvent> dataList = warnEventMapper.selectList(queryMap);
        List<WarnEventVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (WarnEvent warnEvent : dataList) {
            WarnEventVo warnEventVo = BeanUtils.beanCopy(warnEvent, WarnEventVo.class);
            newDataList.add(warnEventVo);
        }
        return pagination;
    }

    @Override
    public WarnEvent findById(Long id) {
        return warnEventMapper.findById(id);
    }


    @Override
    public int insert(WarnEvent warnEvent) {
        return warnEventMapper.insert(warnEvent);
    }

    @Override
    public int updateById(WarnEvent warnEvent) {
        return warnEventMapper.updateById(warnEvent);
    }

    @Override
    public int deleteById(Long id) {
        return warnEventMapper.deleteById(id);
    }

    @Transactional
    public void sendTask(List<WarnEvent> warnEventList){
        for (WarnEvent warnEvent : warnEventList) {
            String userIds = warnEvent.getUserIds();
            String[] userIdArr = userIds.split(",");
            for (String userId : userIdArr) {
                User user = userService.findById(Long.valueOf(userId));
                if (warnEvent.getSms() == YesNoIntType.yes.getCode()) {//send sms
                    String mobile = user.getMobile();
                    boolean success = true;
                    try{
                        SendMsgUtils.sendSMS(161318L,mobile, Arrays.asList(warnEvent.getContent()));
                    }catch (Exception e){
                        if (e instanceof BizException) {
                            BizException bizException = (BizException) e;
                            if (bizException.getCode() == AppConstant.ExceptionCode.REMOTE_TIME_OUT) {
                                success = false;
                            }
                        }
                    }
                    WarnEventResult warnEventResult = new WarnEventResult();
                    warnEventResult.setWarnEventId(warnEvent.getId());
                    warnEventResult.setStatus(success ? YesNoIntType.yes.getCode() : YesNoIntType.no.getCode());
                    warnEventResult.setType(WarnEventResultType.sms.getCode());
                    warnEventResult.setTryCount(0);
                    warnEventResult.setContent(warnEvent.getContent());
                    warnEventResult.setUserId(Long.valueOf(userId));
                    warnEventResultService.insert(warnEventResult);

                }
                if (warnEvent.getEmail() == YesNoIntType.yes.getCode()) {//send email
                    boolean success = true;
                    try{
                        SendEmailUtils.sendText(user.getEmail(),warnEvent.getName(),warnEvent.getContent());
                    }catch (Exception e){
                        success = false;
                    }
                    WarnEventResult warnEventResult = new WarnEventResult();
                    warnEventResult.setWarnEventId(warnEvent.getId());
                    warnEventResult.setStatus(success ? YesNoIntType.yes.getCode() : YesNoIntType.no.getCode());
                    warnEventResult.setType(WarnEventResultType.email.getCode());
                    warnEventResult.setTryCount(0);
                    warnEventResult.setContent(warnEvent.getContent());
                    warnEventResult.setUserId(Long.valueOf(userId));
                    warnEventResultService.insert(warnEventResult);
                }
                if (warnEvent.getWeixin() == YesNoIntType.yes.getCode()) {//send weixin,TODO
                    boolean success = true;
                    try{
//                                SendEmailUtils.sendText(user.getEmail(),warnEvent.getName(),warnEvent.getContent());
                    }catch (Exception e){
                        success = false;
                    }
                    WarnEventResult warnEventResult = new WarnEventResult();
                    warnEventResult.setWarnEventId(warnEvent.getId());
                    warnEventResult.setStatus(success ? YesNoIntType.yes.getCode() : YesNoIntType.no.getCode());
                    warnEventResult.setType(WarnEventResultType.weixin.getCode());
                    warnEventResult.setTryCount(0);
                    warnEventResult.setContent(warnEvent.getContent());
                    warnEventResult.setUserId(Long.valueOf(userId));
                    warnEventResultService.insert(warnEventResult);
                }
                //update
                warnEventMapper.updateStatusById(warnEvent.getId(),YesNoIntType.yes.getCode());

            }
        }

    }

    @Override
    public void tryErrorSendTask(List<WarnEventResult> warnEventResultList) {
        for (WarnEventResult warnEventResult : warnEventResultList) {
            String content = warnEventResult.getContent();
            Long warnEventId = warnEventResult.getWarnEventId();
            WarnEvent warnEvent = this.findById(warnEventId);
            if(StringUtils.isEmpty(content)){
                content = warnEvent.getContent();
            }
            User user = userService.findById(warnEventResult.getUserId());
            boolean success = true;
            if (warnEventResult.getType() == WarnEventResultType.sms.getCode()) {//send sms
                String mobile = user.getMobile();
                try {
                    SendMsgUtils.sendSMS(161318L, mobile, Arrays.asList(content));
                } catch (Exception e) {
                    if (e instanceof BizException) {
                        BizException bizException = (BizException) e;
                        if (bizException.getCode() == AppConstant.ExceptionCode.REMOTE_TIME_OUT) {
                            success = false;
                        }
                    }
                }
            }else if (warnEventResult.getType() == WarnEventResultType.email.getCode()) {//send email
                try{
                    SendEmailUtils.sendText(user.getEmail(),warnEvent.getName(),content);
                }catch (Exception e){
                    success = false;
                }

            }else if (warnEventResult.getType() == WarnEventResultType.weixin.getCode()) {//send wx
                try{
//                                SendEmailUtils.sendText(user.getEmail(),warnEvent.getName(),warnEvent.getContent());
                }catch (Exception e){
                    success = false;
                }
            }
            warnEventResult.setStatus(success ? YesNoIntType.yes.getCode() : YesNoIntType.no.getCode());
            warnEventResult.setTryCount(warnEventResult.getTryCount()+1);
            warnEventResultService.updateById(warnEventResult);

        }

    }


}