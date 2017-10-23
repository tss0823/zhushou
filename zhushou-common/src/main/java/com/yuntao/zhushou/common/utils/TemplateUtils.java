package com.yuntao.zhushou.common.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolManager;

import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Velocity 模板处理
 * Created by shan on 2017/8/12.
 */
public class TemplateUtils {

    public static String render(String tplContent, Map<String,Object> dataMap){
        // 初始化并取得Velocity引擎
        VelocityEngine ve = new VelocityEngine();
        ve.init();

        // 取得velocity的上下文context
//        VelocityContext context = new VelocityContext();

        // 加载toolbox
        ToolManager manager = new ToolManager();
        manager.configure("/toolbox.xml");

        Context context = manager.createContext();

        // 把数据填入上下文
        if (MapUtils.isNotEmpty(dataMap)) {
            Set<Map.Entry<String, Object>> entries = dataMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                context.put(entry.getKey(),entry.getValue());
            }
        }
        StringWriter writer = new StringWriter();

        // 转换输出
        ve.evaluate(context, writer, "", tplContent); // 关键方法

        return writer.toString();

    }

    public static void main(String[] args) {
        String content = "my name is $name my age is $age this time is $date.get('yyyy-MM-dd HH:mm:ss') $date.format('yyyy-MM-dd',${myDate})";
        Map<String,Object> dataMap = new HashedMap();
        dataMap.put("name","shengshan.tang");
        dataMap.put("age","31");
        dataMap.put("myDate",new Date());
        String result = render(content, dataMap);
        System.out.printf("result="+result);

    }
}
