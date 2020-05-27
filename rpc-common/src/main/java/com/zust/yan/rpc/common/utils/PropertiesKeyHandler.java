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
    private final static String MONITOR_ADDRESS = "rpc.monitor.address";
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
        String monitorInfos = properties.getProperty(MONITOR_ADDRESS);
        if (!StringUtils.isEmpty(monitorInfos)) {
            String[] monitorInfoArray = monitorInfos.split(";");
            for (String s : monitorInfoArray) {
                String[] monitorInfo = s.split(":");
                if (monitorInfo.length < 2) {
                    continue;
                }
                NetConfigInfo netConfigInfo = NetConfigInfo.builder()
                        .host(monitorInfo[0])
                        .port(Integer.parseInt(monitorInfo[1]))
                        .build();
                log.info("addMonitorInfo  :" + netConfigInfo);
                loadStrategy.addMonitorInfo(netConfigInfo);
            }
        }

    }
}
