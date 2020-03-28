package com.zust.yan.rpc.net.monitor.utils;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.Client;
import com.zust.yan.rpc.net.base.Request;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
public class MonitorClientUtils {
    private volatile static List<NetConfigInfo> monitorInfos =
            Arrays.asList(NetConfigInfo.builder()
                    .address("127.0.0.1")
                    .port(8886)
                    .build());
    private static LongAdder monitorIndex = new LongAdder();
    private static Map<NetConfigInfo, Client> clientMap = new ConcurrentHashMap<>();

    public static void sendToMonitor(Request request) {
        log.info("sendToMonitor :startTime" + System.currentTimeMillis());
        if (monitorInfos == null) {
            monitorInfos = RpcUtils.getMonitorInfos();
        }
        if (monitorInfos == null) {
            log.error("MonitorUtils monitorInfos not init");
            return;
        }
        int pos = (int) monitorIndex.longValue();
        // 考虑并发其实影响也不大
        if (pos > Integer.MAX_VALUE - 10) {
            monitorIndex.reset();
        }
        NetConfigInfo netConfigInfo = monitorInfos.get(pos % monitorInfos.size());
        Client client = null;
        try {
            if (clientMap.containsKey(netConfigInfo)) {
                client = clientMap.get(netConfigInfo);
            } else {
                log.info("MonitorClient created");
                client = new Client(netConfigInfo);
                clientMap.put(netConfigInfo, client);
                client.start();
            }
            client.send(request);
            monitorIndex.increment();
        } catch (InterruptedException e) {
            // 删除异常连接
            clientMap.remove(netConfigInfo);
            e.printStackTrace();
            log.error("MonitorUtils client unknown error");
            if (!client.isClosed()) {
                try {
                    client.close();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    log.error("MonitorUtils client close error");
                }
            }
            log.info("sendToMonitor :endTime" + System.currentTimeMillis());
        }
    }
}
