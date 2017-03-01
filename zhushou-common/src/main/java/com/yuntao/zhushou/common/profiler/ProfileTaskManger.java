/*
 * Copyright 2010-2011 ESunny.com All right reserved. This software is the confidential and proprietary information of
 * ESunny.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with ESunny.com.
 */
package com.yuntao.zhushou.common.profiler;

import com.yuntao.zhushou.common.utils.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;

/**
 * 类ProfileTaskManger.java的实现描述：性能统计监控任务管理
 *
 * @author shengshang.tang 2014年5月19日 下午4:25:25
 */
public class ProfileTaskManger {

    private static Log performanceLog = LogFactory.getLog("performanceLog");
    private static Logger stackLog = LoggerFactory.getLogger("stackLog");


    private static int infoValve = 100; // >= 100

    private static int warnValve = 500; // >= 500

    private static int errorValve = 1000; // >= 1000

    private static int fatalValve = 5000;

    /**
     * 主任务
     */
    private static ThreadLocal<ProfileTask> currentPtLocal = new ThreadLocal<ProfileTask>();

    /**
     * 入栈任务
     */
    private static ThreadLocal<Stack<ProfileTask>> arrayPtInLocal = new ThreadLocal<Stack<ProfileTask>>();

    /**
     * 出栈任务
     */
//    private static ThreadLocal<Stack<ProfileTask>> arrayPtOutLocal = new ThreadLocal<Stack<ProfileTask>>();

    /**
     * 主任务开始
     *
     * @param name
     * @param content
     */
    public static void startFirst(String name, String content) {
        ProfileTask task = new ProfileTask();
        task.setStartTime(System.currentTimeMillis());
        task.setName(name);
        task.setContent(content);
        currentPtLocal.set(task);

        //put top
        Stack<ProfileTask> stack = new Stack<>();
        stack.push(task);
        arrayPtInLocal.set(stack);

    }

    /**
     * 子任务开始
     *
     * @param name
     * @param content
     */
    public static void start(String name, String content) {
        Stack<ProfileTask> stack = arrayPtInLocal.get();
        if (stack == null) {
            return;
        }
        ProfileTask task = new ProfileTask();
        task.setStartTime(System.currentTimeMillis());
        task.setName(name);
        task.setContent(content);
        //获取父级
        ProfileTask parentTask = stack.peek();
        List<ProfileTask> childTaskList = parentTask.getChildList();
        if (CollectionUtils.isEmpty(childTaskList)) {
            childTaskList = new ArrayList<>();
            parentTask.setChildList(childTaskList);
        }
        currentPtLocal.set(task);

        //把自己添加到child列表
        childTaskList.add(task);

        //把自己推到栈顶上
        stack.push(task);

//        arrayPtInLocal.set(stack);

    }

    /**
     * 子任务结束
     */
    public static void end() {
        Stack<ProfileTask> stack = arrayPtInLocal.get();
        if (stack == null) {
            return;
        }
        //出栈
        ProfileTask task = stack.pop();
        // 计算时间
        Long endTime = System.currentTimeMillis();
        task.setEndTime(endTime);

        // 计算级别
        Long time = task.getTime();
        if (time <= infoValve) {
            task.setLevel(Level.INFO);
        } else if (time <= warnValve) {
            task.setLevel(Level.WARNING);
        } else if (time <= errorValve) {
            task.setLevel(Level.FINE);
        } else {
            task.setLevel(Level.FINEST);
        }

        // 入栈
//        Stack<ProfileTask> outStatck = arrayPtOutLocal.get();
//        if (outStatck == null) {
//            outStatck = new Stack<>();
//            arrayPtOutLocal.set(outStatck);
//        }
//        outStatck.push(task);

    }

    /**
     * 主任务结束
     *
     * @param threshold
     */
    public static void endLast(int threshold) {
        Stack<ProfileTask> stack = arrayPtInLocal.get();
        if (stack == null) {
            return;
        }
        ProfileTask task = stack.pop();
        // 计算时间
        Long endTime = System.currentTimeMillis();
        task.setEndTime(endTime);

        boolean isPerPrint = true;
        if (task.getTime() <= threshold) { // 没有超过检测值，则不打印
            isPerPrint = false;
//            String json = JsonUtils.object2Json(task);
//            performanceLog.info(json);
        }

        // 打印日志
        // 主任务
//        StringBuilder sb = new StringBuilder();
//        // sb.append("thread：" + Thread.currentThread().getName());
//        sb.append("main task：[" + task.getName() + "]");
//        sb.append(task.getContent());
//        sb.append(" take time[" + task.getTime() + "]ms");
//        sb.append("\n");

        // 方法栈
//        Stack<ProfileTask> outStack = arrayPtOutLocal.get();
//        if (outStack == null) {
//            stackLog.info(sb.toString());
//            if(isPerPrint){
//                performanceLog.info(sb.toString());
//            }
//            return;
//        }

        // 打印子任务日志
//        printChildTaskLog(new StringBuilder(),task);

//        while (!outStack.isEmpty()) {
//            task = outStack.pop();
//            sb.append("child task：["+task.getName()+"][");
//            sb.append(task.getLevel().toString());
//            sb.append("] ");
//            sb.append(task.getContent());
//            sb.append(" take time[" + task.getTime() + "]ms");
//            sb.append("\n");
//        }
//        stackLog.info(sb.toString());
//        if(isPerPrint){
//            performanceLog.info(sb.toString());
//        }
    }

    private static void printChildTaskLog(StringBuilder sb, ProfileTask task) {
        //
        sb.append("child task：[" + task.getName() + "][");
        sb.append(task.getLevel().toString());
        sb.append("] ");
        sb.append(task.getContent());
        sb.append(" take time[" + task.getTime() + "]ms");
        sb.append("\n");
        //是否有子集
        List<ProfileTask> childTaskList = task.getChildList();
        if (CollectionUtils.isNotEmpty(childTaskList)) {
            for (ProfileTask profileTask : childTaskList) {


            }
        }

    }

    /**
     * 清空
     */
    public static void clear() {
        currentPtLocal.set(null);
        arrayPtInLocal.set(null);
//        arrayPtOutLocal.set(null);
    }

}
