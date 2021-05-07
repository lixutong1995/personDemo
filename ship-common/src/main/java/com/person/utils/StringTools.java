package com.person.utils;

import com.google.common.collect.Maps;
import com.person.constants.MatchMethodEnum;
import com.person.exception.ShipException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 字符串工具类
 * @Author Xutong Li
 * @Date 2021/5/7
 */
public class StringTools {

    private static final String CHARSET_UTF8 = "UTF-8";

    private static final Map<String, Pattern> PATTERN_MAP = Maps.newHashMap();

    /**
     * @Description 是否匹配
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param value
     * @param matchMethod
     * @param matchRule
     * @return boolean
     */
    public static boolean match(String value, Byte matchMethod, String matchRule){
        if(MatchMethodEnum.EQUAL.getCode().equals(matchMethod)){
            return value.equals(matchRule);
        }else if(MatchMethodEnum.REGEX.getCode().equals(matchMethod)){
            //将Pattern 缓存下来，避免反复编译Pattern
            Pattern pattern = PATTERN_MAP.computeIfAbsent(matchRule, k -> Pattern.compile(k));
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }else if(MatchMethodEnum.LIKE.getCode().equals(matchMethod)){
            return value.indexOf(matchRule) != -1;
        }else{
            throw new ShipException("无效的匹配方法");
        }
    }

    /**
     * @Description 字节转字符串
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param
     * @return java.lang.String
     */
    public static String byteToString(byte[] data){
        try {
            return new String(data, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description URL地址加密
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param value
     * @return java.lang.String
     */
    public static String urlEncode(String value){
        try {
            return URLEncoder.encode(value, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description Md5加盐加密
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param value
     * @param salt
     * @return java.lang.String
     */
    public static String Md5Digest(String value, String salt){
        String plainText = value + salt;
        byte[] secretBytes = null;

        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ShipException("没有Md5加密的这个算法！");
        }

        String md5Code = new BigInteger(1, secretBytes).toString(16);
        for(int i = 0; i < 32-md5Code.length(); i++){
            md5Code = "0" + md5Code;
        }
        return md5Code;
    }
}
