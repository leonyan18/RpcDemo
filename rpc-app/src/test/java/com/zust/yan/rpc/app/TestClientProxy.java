package com.zust.yan.rpc.app;

import com.zust.yan.rpc.app.factory.BeanProxyFactory;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.register.service.RedisRegisterServiceImpl;
import com.zust.yan.rpc.register.service.RegisterService;

public class TestClientProxy {
    public static void main(String[] args) {
        NetConfigInfo monitorInfo=NetConfigInfo.builder()
                .host("127.0.0.1")
                .port(8886)
                .build();
        RpcUtils.addMonitorInfo(monitorInfo);
        Sad sad = BeanProxyFactory.createProxy(Sad.class,true);
        RegisterService registerService = new RedisRegisterServiceImpl();
        registerService.sync(true);
        System.out.println(sad.sad("sd"));
        System.out.println(sad.sad("sd"));
        System.out.println(sad.sad("sd"));
    }
}
