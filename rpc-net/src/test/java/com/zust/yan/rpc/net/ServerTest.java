package com.zust.yan.rpc.net;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.base.Server;

public class ServerTest {
    public static void main(String[] args) {
        NetConfigInfo info = NetConfigInfo.builder()
                .port(8888)
                .build();
        Server server = new Server(info, MonitorHandler::new);
        server.start();
    }
}
