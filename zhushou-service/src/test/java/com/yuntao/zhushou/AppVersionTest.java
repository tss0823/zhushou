package com.yuntao.zhushou;

/**
 * Created by shan on 2017/3/18.
 */
public class AppVersionTest {

    public static void main(String[] args) {
        String lastVersionStr = "0.0.1";
        String version = "0.0.2";
        String lastVersionInt = lastVersionStr.replaceAll("\\.", "");
        String versionInt  = version.replaceAll("\\.","");
        if(Integer.valueOf(lastVersionInt) > Integer.valueOf(versionInt)){  //有最新版本,返回更新链接
            System.out.printf("yes");
        }else{
            System.out.printf("no");
        }
//        int actualVersion = Integer.valueOf(versionInt) + 1;

        String versionStr = version.replaceAll("\\.","");
        versionStr = ""+(Integer.valueOf(versionStr) + 1);
        while(versionStr.length() < 3){
            versionStr = "0"+versionStr;
        }
        version = versionStr.charAt(0)+"."+versionStr.charAt(1)+"."+versionStr.charAt(2);
        System.out.printf("version="+version);
    }

}
