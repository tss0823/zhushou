package com.yuntao.zhushou.common;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by shan on 2016/4/5.
 */
public class QueueContainer {

    private static Queue<String> execMsgList = new ConcurrentLinkedQueue<>();

    public static void offerExecMsg(String msg){
        execMsgList.offer(msg);
    }

    public static String takeExecMsg(){
        return execMsgList.poll();
    }
}
