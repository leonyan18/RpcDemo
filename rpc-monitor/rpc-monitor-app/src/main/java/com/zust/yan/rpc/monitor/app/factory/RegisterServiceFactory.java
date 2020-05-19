package com.zust.yan.rpc.spring.factory;

import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.register.service.RedisRegisterServiceImpl;
import com.zust.yan.rpc.register.service.RegisterService;
import com.zust.yan.rpc.register.service.ZookeeperRegisterServiceImpl;

/**
 * @author yan
 */
public class RegisterServiceFactory {
    private static volatile RegisterService registerService;

    public static RegisterService getRegisterService() {
        if (registerService == null) {
            synchronized (RegisterServiceFactory.class) {
                switch (RpcUtils.getRegisterType()) {
                    case "redis":
                        registerService = new RedisRegisterServiceImpl();
                        break;
                    case "zookeeper":
                        registerService = new ZookeeperRegisterServiceImpl();
                        break;
                    default:
                        registerService = new RedisRegisterServiceImpl();
                }
            }
        }
        return registerService;
    }
}
