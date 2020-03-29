package com.zust.yan.rpc.app;

import com.zust.yan.rpc.app.handler.DefaultInvocationHandler;
import com.zust.yan.rpc.app.factory.BeanProxyFactory;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;

public class TestClientProxy {
    public static void main(String[] args) {
        NetConfigInfo info = NetConfigInfo.builder()
                .host("127.0.0.1")
                .port(8888)
                .build();
        NetConfigInfo monitorInfo=NetConfigInfo.builder()
                .host("127.0.0.1")
                .port(8886)
                .build();
        RpcUtils.addProviderNetInfo(Sad.class.getName(), info);
        RpcUtils.addMonitorInfo(monitorInfo);
        Sad sad = BeanProxyFactory.createProxy(Sad.class);
        System.out.println(sad.sad("sd"));
        System.out.println(sad.sad("sd"));
        System.out.println(sad.sad("sd"));
    }
}
