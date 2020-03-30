package com.zust.yan.rpc.common.utils;

import com.zust.yan.rpc.common.base.LoadStrategy;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;

public class PropertiesKeyHandler {
    private final static String PROVIDER_STRATEGY = "rpc.provider.strategy";
    private final static String MONITOR_STRATEGY = "rpc.monitor.strategy";
    private final static String REGISTER_TYPE = "rpc.register.type";
    private final static String REGISTER_HOST = "rpc.register.host";
    private final static String REGISTER_PORT = "rpc.register.port";
    private final static String REGISTER_PASSWORD = "rpc.register.password";

    // todo 待优化
    public static void handleLoadStrategyProperties(Properties properties, LoadStrategy loadStrategy) {
        if (loadStrategy == null) {
            return;
        }
        String providerStrategy = properties.getProperty(PROVIDER_STRATEGY);
        if (StringUtils.isEmpty(providerStrategy)) {
            loadStrategy.setProviderStrategy(providerStrategy);
        }
        String monitorStrategy = properties.getProperty(MONITOR_STRATEGY);
        if (StringUtils.isEmpty(monitorStrategy)) {
            loadStrategy.setMonitorStrategy(monitorStrategy);
        }
        String type = properties.getProperty(REGISTER_TYPE);
        if (StringUtils.isEmpty(type)) {
            loadStrategy.setRegisterType(type);
        }
        NetConfigInfo netConfigInfo = loadStrategy.getRegisterNetConfigInfo();
        String host = properties.getProperty(REGISTER_HOST);
        if (StringUtils.isEmpty(host)) {
            netConfigInfo.setHost(host);
        }
        String port = properties.getProperty(REGISTER_PORT);
        if (StringUtils.isEmpty(port)) {
            netConfigInfo.setPort(Integer.valueOf(port));
        }
        String password = properties.getProperty(REGISTER_PASSWORD);
        if (StringUtils.isEmpty(password)) {
            netConfigInfo.setPassword(password);
        }
    }
}
