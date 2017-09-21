package com.yuntao.zhushou.zplugin;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;

import java.net.URISyntaxException;

/**
 * Created by shan on 2017/9/19.
 */
public class WsUtils {
    private static H5SocketClient h5SocketClient = null;

    protected final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    public static H5SocketClient openWsConnect() {
        final String wsLogPath = ZpluginUtils.getWsLogPath();
        try {
            h5SocketClient = new H5SocketClient(wsLogPath);
            new Thread(new Runnable() {
                boolean isFirstConn = true;

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
                                } else {
                                    isFirstConn = true;
                                    h5SocketClient.close();
                                    h5SocketClient = new H5SocketClient(wsLogPath);
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
        } catch (
                URISyntaxException e)

        {
            e.printStackTrace();
        }
        return h5SocketClient;
    }

    public static H5SocketClient getH5SocketClient() {
        return h5SocketClient;
    }

    public static  boolean isClose(){
        return (h5SocketClient == null || !h5SocketClient.getConnection().getReadyState().name().equals(WebSocket.READYSTATE.CLOSED.name()) ||
                !h5SocketClient.getConnection().getReadyState().name().equals(WebSocket.READYSTATE.NOT_YET_CONNECTED.name()));
    }
    public static  void close(){
        h5SocketClient.close();
    }
}
