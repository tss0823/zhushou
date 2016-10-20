package com.yuntao.zhushou.model.constant;

/**
 * Created by shengshan.tang on 2015/11/24 at 16:45
 */
public interface AppConstant {

    String TABLE_POSTFIX = "tablePostfix";

    String AUTH_USER = "auth_user";
    String AUTH_PWD = "auth_pwd";

    interface ResponseLevel{
        String INFO = "info";
        String WARN = "warn";
        String ERROR = "error";
    }

    interface ResponseType{

        String NORMAL = "normal";
        String DEPLOY_SCRIPT = "deploy_script";
        String MONITOR_SERVER = "monitor_server";
        String MONITOR_STATUS = "monitor_status";
        String TEST_HTTP_EXCUTE = "test_http_execute";
        String SERVER_STATUS_CHECK = "server_status_check";
    }

    interface ResponseCode{
        String NORMAL = "00";
        String NOT_LOGIN = "01";
        String NOT_AUTHORITY = "02";
        String SYSTEM_ERROR = "03";
    }

    interface AppLog{
        String APP_LOG_SHOW_ALL = "app_log_show_all";
    }

    interface Aliyun{
        String KEY = "LTAINYZHsqPLGKju";
        String SECRET = "9UMnWw0eXXaZgaJuOiNoJYGXHAwv0y";
    }


}
