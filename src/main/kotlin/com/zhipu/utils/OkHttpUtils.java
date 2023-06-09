package com.zhipu.utils;

import okhttp3.OkHttpClient;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {
    private static OkHttpClient okHttpClient;

    public static OkHttpClient getInstance() {
        if (okHttpClient == null) { //加同步安全
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) { //okhttp可以缓存数据....指定缓存路径
                    okHttpClient = new OkHttpClient.Builder()//构建器
                            .proxy(Proxy.NO_PROXY) //来屏蔽系统代理
                            .connectTimeout(10, TimeUnit.SECONDS)//连接超时
                            .writeTimeout(10, TimeUnit.SECONDS)//写入超时
                            .readTimeout(10, TimeUnit.SECONDS)//读取超时
                            .build();
                }
            }
        }
        return okHttpClient;
    }
}
