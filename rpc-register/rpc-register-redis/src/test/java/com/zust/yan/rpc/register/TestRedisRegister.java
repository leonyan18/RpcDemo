package com.zust.yan.rpc.register;

import com.zust.yan.rpc.register.service.RedisService;

public class TestRedisRegister {
    public static void main(String[] args) {
        RedisService redisService = new RedisService();
        redisService.registerService("127.0.0.1", 5646, "com.zust.yan.rpc.register.service.RedisService");
        redisService.registerService("127.0.0.1", 8888, "com.zust.yan.rpc.register.service.TestService");
        System.out.println(redisService.getAllServiceNetInfo());
        System.out.println(redisService.getServiceNetInfo("com.zust.yan.rpc.register.service.RedisService"));
        redisService.removeResistedService("127.0.0.1", 8888, "com.zust.yan.rpc.register.service.TestService");
        System.out.println(redisService.getAllServiceNetInfo());
        System.out.println(redisService.getServiceNetInfo("com.zust.yan.rpc.register.service.RedisService"));
    }
}
