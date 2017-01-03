package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.MsgRequestObject;
import com.yuntao.zhushou.common.web.MsgResponseObject;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.query.AppQuery;
import com.yuntao.zhushou.model.vo.AppVo;

import java.util.List;

public interface WsMsgHandlerService {

    /**
     * 接收消息处理
     * @param responseObject
     */
    void receiveHandler(MsgRequestObject requestObject);

    void sendHandler(MsgResponseObject responseObject);




}
