package com.yuntao.zhushou.common.constant;

/**
 * Created by shengshan.tang on 2015/11/24 at 16:45
 */
public interface CacheConstant {

    interface App{
        String selectAllList = "selectAllList_v2";
    }

    interface Host{
        String selectListByAll = "selectListByAll_v2";
        String selectList = "selectList";
    }

    interface Msg{

//        /** 用户初始化消息key */
//        String userMsgInit = "userMsgInit";

        /** 待发送消息队列key */
        String readyMsgList = "ready_msg_list";

        /** 已发送消息队列key */
        String sentMsgList = "sent_msg_list";

        /** 登录用户key */
        String msgLoginUser = "msg_login_user";



    }


}
