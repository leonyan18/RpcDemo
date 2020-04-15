package com.zust.yan.rpc.register;

import com.zust.yan.rpc.register.service.RedisRegisterServiceImpl;

public class TestRedisRegister {
    public static void main(String[] args) {
        RedisRegisterServiceImpl redisRegisterServiceImpl = new RedisRegisterServiceImpl();
        redisRegisterServiceImpl.registerService("127.0.0.1", 5646, "com.zust.yan.rpc.register.service.RedisService");
        redisRegisterServiceImpl.registerService("127.0.0.1", 8888, "com.zust.yan.rpc.register.service.TestService");
        System.out.println(redisRegisterServiceImpl.getAllServiceNetInfo());
        System.out.println(redisRegisterServiceImpl.getServiceNetInfo("com.zust.yan.rpc.register.service.RedisService"));
        redisRegisterServiceImpl.removeResistedService("127.0.0.1", 8888, "com.zust.yan.rpc.register.service.TestService");
        System.out.println(redisRegisterServiceImpl.getAllServiceNetInfo());
        System.out.println(redisRegisterServiceImpl.getServiceNetInfo("com.zust.yan.rpc.register.service.RedisService"));
    }
}
