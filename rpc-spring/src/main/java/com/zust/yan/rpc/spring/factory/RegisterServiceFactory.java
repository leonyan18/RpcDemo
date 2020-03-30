package com.zust.yan.rpc.spring.factory;

import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.register.service.RedisRegisterService;
import com.zust.yan.rpc.register.service.RegisterService;

public class RegisterServiceFactory {
    public static RegisterService getRegisterService() {
        RegisterService registerService;
        switch (RpcUtils.getRegisterType()) {
            case "redis":
                registerService = new RedisRegisterService();
                break;
            default:
                registerService = new RedisRegisterService();
        }
        return registerService;
    }
}
