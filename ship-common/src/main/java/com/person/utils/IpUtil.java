package com.person.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description IP地址工具类
 * @Author Xutong Li
 * @Date 2021/3/5
 */
public class IpUtil {

    /**
     * @Description 获取ip地址
     * @Author Xutong Li
     * @Date 2021/3/5
     * @param
     * @return java.lang.String
     */
    public static String  getLocalIpAddress(){
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address.getHostAddress();
    }
}
