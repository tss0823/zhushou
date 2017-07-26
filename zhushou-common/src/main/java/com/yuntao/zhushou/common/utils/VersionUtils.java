package com.yuntao.zhushou.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by shan on 2017/7/15.
 */
public class VersionUtils {

    public static String dcrVersion(String lastVersion) {
        if(StringUtils.isEmpty(lastVersion)){
            lastVersion = "0.0.0";
        }
        String versionStr = lastVersion.replaceAll("\\.","");
        versionStr = ""+(Integer.valueOf(versionStr) + 1);
        while(versionStr.length() < 3){
            versionStr = "0"+versionStr;
        }
        return versionStr.charAt(0)+"."+versionStr.charAt(1)+"."+versionStr.charAt(2);
    }

    public static int toNumber(String version) {
        if(StringUtils.isEmpty(version)){
            return 0;
        }
        String versionStr = version.replaceAll("\\.","");
        return Integer.valueOf(versionStr);
    }
}
