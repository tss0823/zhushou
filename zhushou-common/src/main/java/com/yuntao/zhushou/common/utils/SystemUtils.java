package com.yuntao.zhushou.common.utils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by shengshan.tang on 9/21/2015 at 9:59 AM
 */
public class SystemUtils {

    public static String getIp() {
        return getSystemIP("eth1");
    }

    public static String getLocalIp() {
        return getSystemIP("eth0");
    }

    /**
     * 默认外网地址
     * netType : 网络类型：内网、外网
     */
    private static String getSystemIP(String netType) {
        try {
            //非linux平台ip
            if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS || org.apache.commons.lang3.SystemUtils.IS_OS_MAC || org.apache.commons.lang3.SystemUtils.IS_OS_MAC_OSX) {
                return InetAddress.getLocalHost() == null ? "" : InetAddress.getLocalHost().getHostAddress().toString();
            }
            //linux 平台返回ip
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                String network = ni.getName();
                if (netType.equalsIgnoreCase(network)) {
                    Enumeration<InetAddress> inetAddres = ni.getInetAddresses();
                    while (inetAddres.hasMoreElements()) {
                        InetAddress ip = inetAddres.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            Inet4Address ipv4 = (Inet4Address) ip;
                            return ipv4.getHostAddress();
                        } else if (ip != null && ip instanceof Inet6Address) {
                            Inet6Address ipv6 = (Inet6Address) ip;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "00";  //返回错误处理ip
        }
        return "01";  //返回没有结果ip
    }
}
