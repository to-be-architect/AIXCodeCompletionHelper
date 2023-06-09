package com.zhipu.utils;

import com.alibaba.fastjson.JSON;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;

import java.util.HashMap;
import java.util.Map;

public class WuDaoUtils {


    /**
     * 获取鉴权token
     *
     * @param createTokenUrl 获取token
     * @param apiKey
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getToken(String createTokenUrl, String apiKey, String publicKey) throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String encrypt = RSAUtil.encrypt(timestamp, publicKey);
        Map paramsMap = new HashMap(2);
        paramsMap.put("encrypted", encrypt);
        paramsMap.put("apiKey", apiKey);
        String response = HttpUtilClient.sendPostJson(createTokenUrl, JSON.toJSONString(paramsMap));
        Map resultMap = JSON.parseObject(response, Map.class);
        return resultMap;
    }

    /**
     * 引擎请求
     *
     * @param engineUrl 引擎请求地址
     * @param authToken 鉴权token
     * @param paramsMap
     * @return
     * @throws Exception
     */
    public static Map executeEngine(String engineUrl, String authToken, Map paramsMap) throws Exception {
        Map header = new HashMap(1);
        header.put("Authorization", authToken);
        String response = HttpUtilClient.sendPostJson(engineUrl, JSON.toJSONString(paramsMap), header);
        Map resultMap = JSON.parseObject(response, Map.class);
        return resultMap;
    }

    public static Map getTaskOrderResult(String url, String authToken, String taskOrderNo) throws Exception {
        Map header = new HashMap(1);
        header.put("Authorization", authToken);
        String response = HttpUtilClient.sendGet(url + "/" + taskOrderNo, null, header);
        Map resultMap = JSON.parseObject(response, Map.class);
        return resultMap;
    }

    public static void executeSSE(String url, String authToken, SSEListener eventSourceListener, Map<String, Object> paramsMap) throws Exception {
        RequestBody formBody = RequestBody.create(JSON.toJSONString(paramsMap), MediaType.parse("application/json; charset=utf-8"));
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.addHeader("Authorization", authToken);
        Request request = requestBuilder.url(url).post(formBody).build();
        EventSource.Factory factory = EventSources.createFactory(OkHttpUtils.getInstance());
        //创建事件
        factory.newEventSource(request, eventSourceListener);
        eventSourceListener.getCountDownLatch().await();
    }
}
