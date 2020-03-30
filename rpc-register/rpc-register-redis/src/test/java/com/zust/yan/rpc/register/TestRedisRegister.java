package com.zust.yan.rpc.register;

import com.zust.yan.rpc.register.service.RedisRegisterService;

public class TestRedisRegister {
    public static void main(String[] args) {
        RedisRegisterService redisRegisterService = new RedisRegisterService();
        redisRegisterService.registerService("127.0.0.1", 5646, "com.zust.yan.rpc.register.service.RedisService");
        redisRegisterService.registerService("127.0.0.1", 8888, "com.zust.yan.rpc.register.service.TestService");
        System.out.println(redisRegisterService.getAllServiceNetInfo());
        System.out.println(redisRegisterService.getServiceNetInfo("com.zust.yan.rpc.register.service.RedisService"));
        redisRegisterService.removeResistedService("127.0.0.1", 8888, "com.zust.yan.rpc.register.service.TestService");
        System.out.println(redisRegisterService.getAllServiceNetInfo());
        System.out.println(redisRegisterService.getServiceNetInfo("com.zust.yan.rpc.register.service.RedisService"));
    }
}
