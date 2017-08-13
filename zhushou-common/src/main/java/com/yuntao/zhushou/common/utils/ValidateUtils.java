package com.yuntao.zhushou.common.utils;

import com.yuntao.zhushou.common.exception.BizException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by shengshan.tang on 2015/6/1 at 17:24.
 * 后台校验工具
 */
public class ValidateUtils {

    public static void notNull(Object obj,String message){
        if(obj == null){
            throw new BizException(message);
        }
    }
    public static void notEmpty(String str,String message){
        if(StringUtils.isEmpty(str)){
            throw new BizException(message);
        }
    }
    public static void notEmpty(List dataList, String message){
        if(CollectionUtils.isEmpty(dataList)){
            throw new BizException(message);
        }
    }
    public static void maxLen(String str,int maxLen,String message){
        if(StringUtils.isNotEmpty(str) && str.length() > maxLen){
            throw new BizException(message);
        }
    }
}
