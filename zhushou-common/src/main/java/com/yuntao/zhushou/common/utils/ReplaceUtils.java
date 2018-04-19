package com.yuntao.zhushou.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by tangshengshan on 16-10-24.
 */
public class ReplaceUtils {

    /**
     * 替换
     * @param str
     * @param paramMap
     * @return
     */
    public static String replaceAll(String str,Map<String,String> paramMap){
        Set<Map.Entry<String, String>> set = paramMap.entrySet();
        for(Map.Entry<String, String> entry : set){
            String key = entry.getKey();
            String value = entry.getValue();
            str = str.replaceAll("_"+key+"_", value);
        }
        return str;
    }

    public static void main(String[] args) {
        String str = "_aa_afdsaf_bb_";
        Map<String,String> paramMap = new HashMap<String, String>();
        paramMap.put("aa", "jinshan");
        paramMap.put("bb", "duba");
        String result = replaceAll(str, paramMap);
        System.out.println(result);

    }
}
