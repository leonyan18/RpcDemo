package com.zust.yan.rpc.app;

import com.zust.yan.rpc.app.handler.DefaultServerMessageHandler;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.base.Server;

public class TestServerProxy {
    public static void main(String[] args) {
        NetConfigInfo info = NetConfigInfo.builder()
                .port(8888)
                .build();
        DefaultServerMessageHandler.addHandler((Sad) word -> "okkkkkkkk");
        Server server = new Server(info, DefaultServerMessageHandler::new);
        server.start();
    }
}
