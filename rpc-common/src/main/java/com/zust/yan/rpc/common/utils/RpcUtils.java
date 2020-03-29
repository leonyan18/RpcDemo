package com.zust.yan.rpc.common.utils;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.chooser.Chooser;
import com.zust.yan.rpc.common.chooser.ChooserFactory;

import java.util.ArrayList;
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
    private static volatile List<NetConfigInfo> monitorInfos = new ArrayList<>();
    private static volatile String PROVIDER_STRATEGY = "polling";
    private static volatile String MONITOR_STRATEGY = "polling";
    private static volatile Map<String, Chooser> providerChooserMap = new ConcurrentHashMap<>(20);
    ;
    private static volatile Chooser monitorChooser;
    private static Map<String, List<NetConfigInfo>> providerNetInfoMap = new ConcurrentHashMap<>(20);

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

    public static NetConfigInfo getMonitorInfo() {
        if (monitorChooser == null) {
            monitorChooser = ChooserFactory.getChooser(MONITOR_STRATEGY);
        }
        return monitorChooser.chooseNetConfigInfo(monitorInfos);
    }

    public static void addMonitorInfo(NetConfigInfo netConfigInfo) {
        monitorInfos.add(netConfigInfo);
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

    public static Map<String, List<NetConfigInfo>> getProviderNetInfoMap() {
        return providerNetInfoMap;
    }

    public static NetConfigInfo getProviderNetInfo(String clazz) {
        Chooser chooser = providerChooserMap.get(clazz);
        if (chooser == null) {
            chooser = ChooserFactory.getChooser(PROVIDER_STRATEGY);
            providerChooserMap.put(clazz, chooser);
        }
        return chooser.chooseNetConfigInfo(providerNetInfoMap.get(clazz));
    }

    public static void addProviderNetInfo(String clazz, NetConfigInfo netConfigInfo) {
        if (!providerNetInfoMap.containsKey(clazz)) {
            providerNetInfoMap.put(clazz, new ArrayList<>());
        }
        providerNetInfoMap.get(clazz).add(netConfigInfo);
    }
}
