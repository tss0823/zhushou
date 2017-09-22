package com.yuntao.zhushou.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtil {
    public static final long MILLION = 1000000;

    public static String formatUserId(long id) {
        return new DecimalFormat("0000").format(id).substring(0,4);
    }

    /**
     * 相加
     * @param num1
     * @param num2
     * @return
     */
    public static Integer add(Integer num1,Integer num2){
        if(num1 == null){
            num1 = 0;
        }
        if(num2 == null){
            num2 = 0;
        }
        return num1 + num2;
    }
    /**
     * 相减
     * @param num1
     * @param num2
     * @return
     */
    public static Integer sub(Integer num1,Integer num2){
        if(num1 == null){
            num1 = 0;
        }
        if(num2 == null){
            num2 = 0;
        }
        return num1 - num2;
    }

    public static boolean equals(Integer num1,Integer num2){
        return num1 == null ? num2 == null : num1.equals(num2);
    }

    /**
     * 相加
     * @param num1
     * @param num2
     * @return
     */
    public static Long add(Long num1,Long num2){
        if(num1 == null){
            num1 = 0L;
        }
        if(num2 == null){
            num2 = 0L;
        }
        return num1 + num2;
    }

    public static Long getNumber(Long value){
        return (value == null ? 0 : value);
    }

    public static Long getNumber(Object value){
        if(value != null){
            try{
                return Long.valueOf(value.toString()) ;
            }catch (Exception e){}
        }
        return 0L;
    }

    public static Integer getNumber(Integer value){
        return (value == null ? 0 : value);
    }

    public static int getRate(Integer divisor,Integer dividend){
        if(divisor == 0 || divisor == null){
            return 0;
        }
        int rate =  BigDecimal.valueOf(divisor).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(dividend),1,BigDecimal.ROUND_HALF_UP).intValue();
        return rate;
    }
}
