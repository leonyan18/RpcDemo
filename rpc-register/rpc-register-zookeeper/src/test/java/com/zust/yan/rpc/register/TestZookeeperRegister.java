package com.zust.yan.rpc.register;

import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.register.service.RegisterService;
import com.zust.yan.rpc.register.service.ZookeeperRegisterServiceImpl;

public class TestZookeeperRegister {
    public static void main(String[] args) throws InterruptedException {
        RpcUtils.init();
        RegisterService registerService = new ZookeeperRegisterServiceImpl();
        registerService.sync(false);
        registerService.registerService("127.0.0.1", 5646, "com.zust.yan.rpc.register.service.RedisService");
        registerService.registerService("127.0.0.1", 8888, "com.zust.yan.rpc.register.service.TestService");
        System.out.println(registerService.getAllServiceNetInfo());
        System.out.println(registerService.getServiceNetInfo("com.zust.yan.rpc.register.service.RedisService"));
//        registerService.removeResistedService("127.0.0.1", 8888, "com.zust.yan.rpc.register.service.TestService");
        System.out.println(registerService.getAllServiceNetInfo());
        System.out.println(registerService.getServiceNetInfo("com.zust.yan.rpc.register.service.RedisService"));
        Thread.sleep(100000);
    }
}
