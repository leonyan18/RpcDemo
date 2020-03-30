package com.zust.yan.rpc.common.base;

import com.zust.yan.rpc.common.chooser.Chooser;
import com.zust.yan.rpc.common.chooser.ChooserFactory;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yan
 */
@Data
public class LoadStrategy {
    private String providerStrategy = "polling";
    private String monitorStrategy = "polling";
    private NetConfigInfo registerNetConfigInfo = NetConfigInfo.builder()
            .port(6379)
            .host("127.0.0.1")
            .timeOut(2000)
            .build();
    private String registerType = "redis";
    private Map<String, Chooser> providerChooserMap = new ConcurrentHashMap<>(20);
    private Chooser monitorChooser;
    private List<NetConfigInfo> monitorInfos = new ArrayList<>();
    private Map<String, List<NetConfigInfo>> providerNetInfoMap = new ConcurrentHashMap<>(20);

    public NetConfigInfo getMonitorInfo() {
        if (monitorChooser == null) {
            monitorChooser = ChooserFactory.getChooser(monitorStrategy);
        }
        return monitorChooser.chooseNetConfigInfo(monitorInfos);
    }

    public void addMonitorInfo(NetConfigInfo netConfigInfo) {
        monitorInfos.add(netConfigInfo);
    }

    public NetConfigInfo getProviderNetInfo(String clazz) {
        Chooser chooser = providerChooserMap.get(clazz);
        if (chooser == null) {
            chooser = ChooserFactory.getChooser(providerStrategy);
            providerChooserMap.put(clazz, chooser);
        }
        return chooser.chooseNetConfigInfo(providerNetInfoMap.get(clazz));
    }

    public void addProviderNetInfo(String clazz, NetConfigInfo netConfigInfo) {
        if (!providerNetInfoMap.containsKey(clazz)) {
            providerNetInfoMap.put(clazz, new ArrayList<>());
        }
        List<NetConfigInfo> netConfigInfoList = providerNetInfoMap.get(clazz);
        // todo 复杂度？
        if (!netConfigInfoList.contains(netConfigInfo)) {
            providerNetInfoMap.get(clazz).add(netConfigInfo);
        }
    }
}
