package com.yuntao.zhushou.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class MD5Util {


    public static String MD5Encode(String key) {
        if (StringUtils.isBlank(key)) {
            key = "";
        }
        return DigestUtils.md5Hex(key).toUpperCase();
    }

    public static String MD5EncodeForPwd(String key, String pwd) {
        if (StringUtils.isBlank(key)) {
            key = "";
        }
        if (StringUtils.isBlank(pwd)) {
            pwd = "";
        }
        return DigestUtils.md5Hex(key + "_" + pwd).toUpperCase();
    }

    public static String MD5Encode(byte data[]) {
        return DigestUtils.md5Hex(data).toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(MD5Encode("123456"));
        System.out.println(MD5EncodeForPwd("13758129145", MD5Encode("123456")));
        System.out.println(MD5EncodeForPwd("shan", "E10ADC3949BA59ABBE56E057F20F883E"));
    }
}
