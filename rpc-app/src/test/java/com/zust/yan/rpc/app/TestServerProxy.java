package com.zust.yan.rpc.app;

import com.zust.yan.rpc.app.factory.BeanProxyFactory;
import com.zust.yan.rpc.app.handler.DefaultInvocationHandler;
import com.zust.yan.rpc.app.handler.DefaultServerMessageHandler;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.base.Server;

public class TestServerProxy {
    public static void main(String[] args) {
        NetConfigInfo info=NetConfigInfo.builder()
                .host("127.0.0.1")
                .port(8888)
                .build();
        DefaultServerMessageHandler.addHandler((Happy) word -> "okkkkkkkk");
        DefaultInvocationHandler invocationHandler=new DefaultInvocationHandler();
        invocationHandler.setNetConfigInfo(info);
        Happy happy= BeanProxyFactory.createProxy(Happy.class,invocationHandler);
        DefaultServerMessageHandler.addHandler((Sad) word -> happy.happy("dssd"));
        Server server = new Server(info, DefaultServerMessageHandler::new);
        server.start();
    }
}
