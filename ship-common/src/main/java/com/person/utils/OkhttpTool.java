package com.person.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.person.exception.ShipException;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description http 请求工具类
 * @Author Xutong Li
 * @Date 2021/3/5
 */
public class OkhttpTool {

    private static final String HTTP_JSON = "application/json;charset=utf-8";

    /**
     * Gson 是 Google 提供的用来在 Java 对象和 JSON 数据之间进行映射的 Java 类库。
     * 可以将一个 JSON 字符串转成一个 Java 对象，或者反过来
     */
    private static Gson gson = new GsonBuilder().create();

    private static OkHttpClient client;

    static{
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    /**
     * @Description 发送post请求与body
     * @Author Xutong Li
     * @Date 2021/3/5
     * @param url
     * @param t
     * @return void
     */
    public static <T> void doPost(String url, T t){
        RequestBody requestBody = RequestBody.create(MediaType.parse(HTTP_JSON), gson.toJson(t));
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if(response.code() < 200 || response.code() >= 300){
                throw new ShipException("request " + url + " fail,http code:" + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ShipException("request " + url + " fail");
        }
    }

    /**
     * @Description 发送put请求
     * @Author Xutong Li
     * @Date 2021/3/5
     * @param url
     * @return java.lang.String
     */
    public static String doPut(String url, Map<String, Object> queryParamMap, String body){
        String requestUrl = null;
        if(queryParamMap == null){
            requestUrl = url;
        }else{
            StringBuilder stringBuilder = new StringBuilder(url);
            stringBuilder.append("?");
            for(Map.Entry<String, Object> entry : queryParamMap.entrySet()){
                stringBuilder.append(entry.getKey() + "=" + entry.getValue());
                stringBuilder.append("&");
            }
            requestUrl = stringBuilder.toString();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse(HTTP_JSON), body);
        Request request = new Request.Builder()
                .put(requestBody)
                .url(requestUrl)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if(response.code() < 200 || response.code() >= 300){
                throw new ShipException("request " + url + " fail,http code:" + response.code());
            }
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ShipException("request " + url + " fail");
        }
    }
}
