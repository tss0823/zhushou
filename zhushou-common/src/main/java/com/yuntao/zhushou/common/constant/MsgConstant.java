package com.yuntao.zhushou.common.constant;


/**
 * Created by shengshan.tang on 2015/11/24 at 16:45
 */
public interface MsgConstant {

    String longMsg = "longMsg";


    interface ReqCoreBizType{  //client内核请求业务类型
        String SHELL = "deploy_script";
        String WARN = "monitor_status";
        String EVENT_START = "event_start";
        String EVENT_END = "event_end";
        String UPDATE_ADDRESS = "update_address";  //core 提给 deploy http ip and port
        String SERVER_STATUS_CHECK = "server_status_check";  //服务器节点状态检查
        String FRONT_DEPLOY_END = "front_deploy_end";  //front deploy end
    }

    interface ReqUserBizType{  //client User请求业务类型
        String INIT = "init";  //初始化

    }


    /**
     * 请求业务类型
     */
    interface RequestBizType {
        String NORMAL = "00";
    }


    /**
     * 请求和返回类型
     */
    interface ReqResType {
        String USER = "user";   //企业用户端
        String CORE = "core";  //企业内核端
    }


    //======================  返回 ==================================================//

    /**
     * 返回业务类型
     */
    interface ResponseBizType {

        String NORMAL = "normal";
        String DEPLOY_SCRIPT = "deploy_script";
//        String MONITOR_SERVER = "monitor_server";
        String MONITOR_STATUS = "monitor_status";
        String TEST_HTTP_EXCUTE = "test_http_execute";
        String SERVER_STATUS_CHECK = "server_status_check";
    }


    /**
     * 返回系统编码
     */
    interface ResponseCode {
        String NORMAL = "00";  //正常

        String NOT_LOGIN = "01";  //未登录

        String NOT_AUTHORITY = "02";  //没有权限

        String SYSTEM_ERROR = "03";   //系统错误

        String CONN_OFFLINE = "03";
    }


}
