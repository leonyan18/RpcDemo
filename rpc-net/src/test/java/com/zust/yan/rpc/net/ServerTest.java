package com.zust.yan.rpc.net;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.Response;
import com.zust.yan.rpc.net.base.Server;
import com.zust.yan.rpc.net.handler.ServerMessageHandlerFactory;
import io.netty.channel.*;

public class ServerTest {
    public static void main(String[] args) {
        NetConfigInfo info = NetConfigInfo.builder()
                .port(8886)
                .build();
        Server server = new Server(info, MonitorHandler::new);
        server.start();
    }
}
