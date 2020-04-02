package com.zust.yan.rpc.net.monitor.utils;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.Client;
import com.zust.yan.rpc.net.base.Request;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
public class MonitorClientUtils {
    private static LongAdder monitorIndex = new LongAdder();
    private static Map<NetConfigInfo, Client> clientMap = new ConcurrentHashMap<>();

    public static void sendToMonitor(Request request) {
        log.info("sendToMonitor :startTime" + System.currentTimeMillis());
        NetConfigInfo netConfigInfo = RpcUtils.getMonitorInfo();
        if (netConfigInfo == null) {
            log.error("MonitorUtils monitorInfos not init");
            return;
        }
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

    public static void closeAll() throws InterruptedException {
        for (Client c:clientMap.values()) {
            if(c==null){
                continue;
            }
            if(c.isClosed()){
                continue;
            }
            c.close();
        }
    }
}
