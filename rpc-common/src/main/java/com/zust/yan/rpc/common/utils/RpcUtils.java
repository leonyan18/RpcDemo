package com.zust.yan.rpc.common.utils;

import com.zust.yan.rpc.common.base.NetConfigInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class RpcUtils {
    private static int coreSize = 10;
    private static int maxSize = 20;
    private static int queueLength = 5;
    private static int keepAliveTime = 1000;
    private static volatile List<NetConfigInfo> monitorInfos;

    public static int getMachineCode() {
        return 10005;
    }

    public static Executor getExecutor(String name) {
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(queueLength)
                , new DefaultThreadFactory(name));
    }

    public static List<NetConfigInfo> getMonitorInfos() {
        return monitorInfos;
    }

    public static NetConfigInfo getRegisterInfo() {
        return NetConfigInfo.builder()
                .port(6379)
                .host("127.0.0.1")
                .timeOut(2000)
                .build();
    }

    public static Map<String, Object> getJedisPoolConfig() {
        Map<String, Object> map = new HashMap<>(5);
        map.put("maxIdle", 8);
        map.put("maxTotal", 16);
        return map;
    }

}
