package com.zhipu.utils;

import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.concurrent.CountDownLatch;


public class SSEListener extends EventSourceListener {

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpen(final EventSource eventSource, final Response
            response) {
        System.out.println("建立sse连接...");
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        System.out.println(String.format("finish {%s}: {%s} {%s}", id, type, data));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClosed(final EventSource eventSource) {
        System.out.println("sse连接关闭...");
        countDownLatch.countDown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFailure(final EventSource eventSource, final Throwable t, final Response response) {
        System.out.println("使用事件源时出现异常... [响应：{}]...");
    }

    public CountDownLatch getCountDownLatch() {
        return this.countDownLatch;
    }
}
