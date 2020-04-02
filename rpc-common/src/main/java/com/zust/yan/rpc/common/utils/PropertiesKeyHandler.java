package com.zust.yan.rpc.common.utils;

import com.zust.yan.rpc.common.base.LoadStrategy;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;

@Slf4j
public class PropertiesKeyHandler {
    private final static String PROVIDER_STRATEGY = "rpc.provider.strategy";
    private final static String MONITOR_STRATEGY = "rpc.monitor.strategy";
    private final static String REGISTER_TYPE = "rpc.register.type";
    private final static String REGISTER_HOST = "rpc.register.host";
    private final static String REGISTER_PORT = "rpc.register.port";
    private final static String MONITOR_HOST = "rpc.monitor.host";
    private final static String MONITOR_PORT = "rpc.monitor.port";
    private final static String REGISTER_PASSWORD = "rpc.register.password";

    // todo 待优化
    public static void handleLoadStrategyProperties(Properties properties, LoadStrategy loadStrategy) {
        if (loadStrategy == null) {
            return;
        }
        String providerStrategy = properties.getProperty(PROVIDER_STRATEGY);
        if (!StringUtils.isEmpty(providerStrategy)) {
            loadStrategy.setProviderStrategy(providerStrategy);
        }
        String monitorStrategy = properties.getProperty(MONITOR_STRATEGY);
        if (!StringUtils.isEmpty(monitorStrategy)) {
            loadStrategy.setMonitorStrategy(monitorStrategy);
        }
        String type = properties.getProperty(REGISTER_TYPE);
        if (!StringUtils.isEmpty(type)) {
            loadStrategy.setRegisterType(type);
        }
        NetConfigInfo registerNetConfigInfo = loadStrategy.getRegisterNetConfigInfo();
        String host = properties.getProperty(REGISTER_HOST);
        if (!StringUtils.isEmpty(host)) {
            registerNetConfigInfo.setHost(host);
        }
        String port = properties.getProperty(REGISTER_PORT);
        if (!StringUtils.isEmpty(port)) {
            registerNetConfigInfo.setPort(Integer.valueOf(port));
        }
        String password = properties.getProperty(REGISTER_PASSWORD);
        if (!StringUtils.isEmpty(password)) {
            registerNetConfigInfo.setPassword(password);
        }
        loadStrategy.setRegisterNetConfigInfo(registerNetConfigInfo);
        NetConfigInfo netConfigInfo = NetConfigInfo.builder().build();
        String monitorHost = properties.getProperty(MONITOR_HOST);
        if (!StringUtils.isEmpty(monitorHost)) {
            netConfigInfo.setHost(monitorHost);
        }
        String monitorPort = properties.getProperty(MONITOR_PORT);
        if (!StringUtils.isEmpty(monitorPort)) {
            netConfigInfo.setPort(Integer.valueOf(monitorPort));
        }
        log.info("addMonitorInfo  :"+netConfigInfo);
        loadStrategy.addMonitorInfo(netConfigInfo);
    }
}
