package com.zust.yan.rpc.app;

import com.zust.yan.rpc.app.handler.DefaultInvocationHandler;
import com.zust.yan.rpc.app.factory.BeanProxyFactory;
import com.zust.yan.rpc.common.base.NetConfigInfo;

public class TestClientProxy {
    public static void main(String[] args) {
        NetConfigInfo info=NetConfigInfo.builder()
                .host("127.0.0.1")
                .port(8888)
                .build();
        DefaultInvocationHandler invocationHandler=new DefaultInvocationHandler();
        invocationHandler.setNetConfigInfo(info);
        Sad sad= BeanProxyFactory.createProxy(Sad.class,invocationHandler);
        System.out.println(sad.sad("sd"));
        System.out.println(sad.sad("sd"));
        System.out.println(sad.sad("sd"));
    }
}
