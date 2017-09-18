package com.yuntao.zhushou.zplugin;

import com.intellij.openapi.components.ApplicationComponent;
import com.yuntao.zhushou.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;

/**
 * Created by shan on 2017/9/5.
 */
public class ZhushouComponent implements ApplicationComponent {
    protected final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    protected final static Logger log = org.slf4j.LoggerFactory.getLogger(ZhushouComponent.class);

    private H5SocketClient h5SocketClient;
    private volatile boolean isFirstConn = true; //第一次链接
    public ZhushouComponent() {
    }

    @Override
    public void initComponent() {
        // TODO: insert component initialization logic here
        try {
            String userHomePath = System.getProperty("user.home");
            if (StringUtils.isEmpty(ZpluginUtils.getLogPath())) {
                //设置日志路径
//            String logPath = userHomePath+"/zplugin.log"; File logFile = new File(logPath);
//            if(logFile.exists()){
//                logFile.delete();
//            }            String userHomePath = System.getProperty("user.home");
                //设置日志路径
//            String logPath = userHomePath+"/zplugin.log"; File logFile = new File(logPath);
//            if(logFile.exists()){
//                logFile.delete();
//            }
//            logFile.createNewFile();
                ZpluginUtils.setLogPath(userHomePath);
                //end
            }

            final String fixFilePath = userHomePath+"/ws_console.log";
            File file = new File(fixFilePath);
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            ZpluginUtils.authCheck();

            h5SocketClient = new H5SocketClient(fixFilePath);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (h5SocketClient.getConnection().getReadyState().name().equals(WebSocket.READYSTATE.CLOSED.name()) ||
                                    h5SocketClient.getConnection().getReadyState().name().equals(WebSocket.READYSTATE.NOT_YET_CONNECTED.name())) {
                                if (isFirstConn) {
                                    isFirstConn = false;
                                    h5SocketClient.connect();
                                    bisLog.info("init h5SocketClient ok");
                                }else{
                                    isFirstConn = true;
                                    h5SocketClient.close();
                                    h5SocketClient = new H5SocketClient(fixFilePath);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }).start();

//            logFile.createNewFile();
            ZpluginUtils.setLogPath(userHomePath);
            //end

            final String fixWsFilePath = userHomePath+"/ws_console.log";
            file = new File(fixWsFilePath);
            if(file.exists()){
               file.delete();
            }
            file.createNewFile();
            ZpluginUtils.authCheck();

            h5SocketClient = new H5SocketClient(fixWsFilePath);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (h5SocketClient.getConnection().getReadyState().name().equals(WebSocket.READYSTATE.CLOSED.name()) ||
                                    h5SocketClient.getConnection().getReadyState().name().equals(WebSocket.READYSTATE.NOT_YET_CONNECTED.name())) {
                                if (isFirstConn) {
                                    isFirstConn = false;
                                    h5SocketClient.connect();
                                    bisLog.info("init h5SocketClient ok");
                                }else{
                                    isFirstConn = true;
                                    h5SocketClient.close();
                                    h5SocketClient = new H5SocketClient(fixFilePath);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }).start();

        } catch (Exception e) {
            throw new BizException("init ws failed!",e);
        }

    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
        if (h5SocketClient != null && !h5SocketClient.getConnection().getReadyState().name().equals(WebSocket.READYSTATE.CLOSED.name()) &&
                !h5SocketClient.getConnection().getReadyState().name().equals(WebSocket.READYSTATE.NOT_YET_CONNECTED.name())) {
            h5SocketClient.close();
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
