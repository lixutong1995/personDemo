package com.person.Utils;

import com.google.gson.Gson;
import com.person.pojo.PayLoad;
import com.person.pojo.dto.HeaderDTO;
import org.apache.commons.codec.Charsets;
import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * @Description JWT工具类
 * @Author Xutong Li
 * @Date 2021/5/7
 */
public class JwtUtils {

    /**
     * 加密算法
     */
    private static final String HEADER_ALG = "HS256";

    private static final String HEADER_TYP = "JWT";

    /**
     * 加密串
     */
    private static final String SECRET = "d5ec0a02";

    /**
     * 生成token
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param payLoad
     * @return java.lang.String
     */
    public static String generateToken(PayLoad payLoad) throws Exception {
        Gson gson = new Gson();
        HeaderDTO header = new HeaderDTO(HEADER_ALG, HEADER_TYP);
        String encodeHeader = Base64Utils.encodeToUrlSafeString(gson.toJson(header).getBytes(Charsets.UTF_8));
        String encodePayLoad = Base64Utils.encodeToUrlSafeString(gson.toJson(payLoad).getBytes(Charsets.UTF_8));
        //获取签名
        String signature = HMACSHA256(encodeHeader + "." + encodePayLoad, SECRET);
        return encodeHeader + "." + encodePayLoad + "." + signature;
    }

    /**
     * 校验签名
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param token
     * @return boolean
     */
    public static boolean checkSignature(String token){
        String[] array = token.split("\\.");
        if(array.length != 3){
            throw new IllegalArgumentException("token错误！！！");
        }

        try {
            String signature = HMACSHA256(array[0] + "." + array[1], SECRET);
            return array[2].equals(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过token获取PayLoad
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param token
     * @return com.person.pojo.PayLoad
     */
    public static PayLoad getPayLoad(String token){
        String[] array = token.split("\\.");
        if(array.length != 3){
            throw new IllegalArgumentException("token错误！！！");
        }
        String payload = new String(Base64Utils.decodeFromUrlSafeString(array[1]), Charsets.UTF_8);
        Gson gson = new Gson();
        return gson.fromJson(payload, PayLoad.class);
    }

    /**
     * HMACSHA256加密
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param data
     * @param key
     * @return java.lang.String
     */
    private static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");

        sha256_HMAC.init(secret_key);

        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

        StringBuilder stringBuilder = new StringBuilder(64);

        for(byte item : array){
            stringBuilder.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }

        return stringBuilder.toString().toUpperCase();
    }
}
