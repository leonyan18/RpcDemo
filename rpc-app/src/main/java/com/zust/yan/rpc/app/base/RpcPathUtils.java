package com.zust.yan.rpc.app.base;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcPathUtils {
    private static ThreadLocal<Long> lastRequest = new ThreadLocal<>();
    private static ThreadLocal<Long> firstRequestId = new ThreadLocal<>();

    public static void beforeHandle(Long requestId) {
        // 上一次请求id
        // 初始化
        if (firstRequestId.get() == null) {
            firstRequestId.set(requestId);
        } else {
            // 记录这次请求路径
            lastRequest.set(requestId);
        }
    }

    public static Long afterHandle(Long requestId) {
        log.info(Thread.currentThread().getName());
        // 如果已经回到起点就清空数据
        Long lastRequestId = lastRequest.get();
        if (firstRequestId.get().equals(requestId)) {
            firstRequestId.remove();
            lastRequest.remove();
        }
        return lastRequestId;
    }
}
