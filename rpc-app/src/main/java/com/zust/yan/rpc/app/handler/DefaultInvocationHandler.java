package com.zust.yan.rpc.app.handler;

import com.zust.yan.rpc.net.base.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author yan
 */
public class DefaultInvocationHandler implements InvocationHandler {
    private NetConfigInfo netConfigInfo;
    private Client client;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (client == null) {
            createClient();
        }
        RequestMethodInfo methodInfo = new RequestMethodInfo(method, args);
        DefaultFuture defaultFuture = client.send(new Request(methodInfo));
        return defaultFuture.getResBlock().getData();
    }

    public NetConfigInfo getNetConfigInfo() {
        return netConfigInfo;
    }

    public void setNetConfigInfo(NetConfigInfo netConfigInfo) {
        this.netConfigInfo = netConfigInfo;
    }

    private void createClient() throws InterruptedException {
        NetConfigInfo info = NetConfigInfo.builder()
                .address("127.0.0.1")
                .port(8888)
                .build();
        client = new Client(info);
        client.start();
    }
}
