package com.zust.yan.rpc.common.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class RpcUtils {
    private static int coreSize = 10;
    private static int maxSize = 20;
    private static int queueLength = 5;
    private static int keepAliveTime = 1000;

    public static int getMachineCode() {
        return 1091005;
    }

    public static Executor getExecutor(String name) {
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(queueLength)
                , new DefaultThreadFactory(name));
    }
}
