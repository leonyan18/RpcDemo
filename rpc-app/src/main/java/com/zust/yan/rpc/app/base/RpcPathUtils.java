package com.zust.yan.rpc.app.base;

import com.zust.yan.rpc.net.base.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RpcPathUtils {
    private static ThreadLocal<Long> lastRequest = new ThreadLocal<>();
    private static ThreadLocal<Long> firstRequestId = new ThreadLocal<>();

    public static void beforeHandle(Long requestId) {
        // 上一次请求id
        System.out.println(Thread.currentThread().getName());
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
