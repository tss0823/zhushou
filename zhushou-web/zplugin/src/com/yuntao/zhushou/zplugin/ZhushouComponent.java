package com.yuntao.zhushou.zplugin;

import com.intellij.openapi.components.ApplicationComponent;
import com.yuntao.zhushou.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;

/**
 * Created by shan on 2017/9/5.
 */
public class ZhushouComponent implements ApplicationComponent {
    protected final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    protected final static Logger log = org.slf4j.LoggerFactory.getLogger(ZhushouComponent.class);

//    private H5SocketClient h5SocketClient;
//    private volatile boolean isFirstConn = true; //第一次链接
    public ZhushouComponent() {
    }

    @Override
    public void initComponent() {
        // TODO: insert component initialization logic here
        try {

            String userHomePath = System.getProperty("user.home");
            //设置日志路径
            if (StringUtils.isEmpty(ZpluginUtils.getLogPath())) {
                ZpluginUtils.setLogPath(userHomePath);
            }

            //ws Log path
            final String wsLogPath = userHomePath+"/ws_console.log";
            File file = new File(wsLogPath);
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();

            ZpluginUtils.setWsLogPath(wsLogPath);
            //end

            if (ZpluginUtils.isLogin()) {
                WsUtils.openWsConnect();

                //设置app ports
//                String appPorts = ZpluginUtils.getValue(ZpluginConstant.appPorts);
//                if(StringUtils.isEmpty(appPorts)){
//
//                }

            }

        } catch (Exception e) {
            throw new BizException("init ws failed!",e);
        }

    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
        if (!WsUtils.isClose()) {
            WsUtils.close();
            bisLog.info("close h5SocketClient ok");
        }
        bisLog.info("disposeComponent ok");
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "ZhushouComponent";
    }
}
