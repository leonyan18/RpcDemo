package com.zust.yan.rpc.common.utils;

import com.zust.yan.rpc.common.base.LoadStrategy;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.base.ThreadPoolInfo;
import com.zust.yan.rpc.common.chooser.ChooserBuilder;
import com.zust.yan.rpc.common.chooser.ChooserFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * @author yan
 */
@Slf4j
public class RpcUtils {
    private static ThreadPoolInfo threadPoolInfo = new ThreadPoolInfo();
    private static LoadStrategy loadStrategy = new LoadStrategy();
    private static int localPort = 8886;
    public static long timeOut = 5000;
    public static long reTryTimes = 3;
    public static long failTimes = 3;
    public static int machineCode = 10005;

    public static int getMachineCode() {
        return machineCode;
    }

    public static Executor getExecutor(String name) {
        return threadPoolInfo.getExecutor(name);
    }

    public static List<NetConfigInfo> getMonitorInfos() {
        return loadStrategy.getMonitorInfos();
    }

    public static NetConfigInfo getMonitorInfo() {
        return loadStrategy.getMonitorInfo();
    }

    public static void addMonitorInfo(NetConfigInfo netConfigInfo) {
        loadStrategy.addMonitorInfo(netConfigInfo);
    }

    public static NetConfigInfo getRegisterInfo() {
        return loadStrategy.getRegisterNetConfigInfo();
    }

    public static Map<String, Object> getJedisPoolConfig() {
        Map<String, Object> map = new HashMap<>(5);
        map.put("maxIdle", 8);
        map.put("maxTotal", 16);
        return map;
    }

    public static Map<String, List<NetConfigInfo>> getProviderNetInfoMap() {
        return loadStrategy.getProviderNetInfoMap();
    }

    public static void setProviderNetInfoMap(Map<String, List<NetConfigInfo>> map) {
        // 转化成并发map
        map = new ConcurrentHashMap<>(map);
        loadStrategy.setProviderNetInfoMap(map);
    }

    public static NetConfigInfo getProviderNetInfo(String clazz) {
        return loadStrategy.getProviderNetInfo(clazz);
    }

    public static void addProviderNetInfo(String clazz, NetConfigInfo netConfigInfo) {
        loadStrategy.addProviderNetInfo(clazz, netConfigInfo);
    }

    public static NetConfigInfo getLocalServerNetInfo() {
        return NetConfigInfo.builder()
                .host("127.0.0.1")
                .port(localPort)
                .build();
    }

    public static String getRegisterType() {
        return loadStrategy.getRegisterType();
    }

    public static void clearServiceNetInfo() {
        loadStrategy.clearServiceNetInfo();
    }

    public static void init() {
        InputStream in = RpcUtils.class.getResourceAsStream("/rpc.properties");
        Properties props = new Properties();
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            props.load(inputStreamReader);
            if (props.getProperty("rpc.local.server.port") != null) {
                localPort = Integer.valueOf(props.getProperty("rpc.local.server.port"));
            }
            PropertiesKeyHandler.handleLoadStrategyProperties(props, loadStrategy);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeServiceNetInfo(String clazz, String address) {
        loadStrategy.removeServiceNetInfo(clazz, address);
    }

    public static void setChooserBuilder(ChooserBuilder chooserBuilder) {
        ChooserFactory.setChooserBuilder(chooserBuilder);
    }

    public static void main(String[] args) {
        RpcUtils.init();
    }
}
