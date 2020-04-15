package com.zust.yan.rpc.app.factory;

import com.zust.yan.rpc.app.handler.DefaultInvocationHandler;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.DefaultFuture;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yan
 */
@Slf4j
public class BeanProxyFactory {

    public static <T> T createProxy(Class<T> interfaceClass) {
        // 先不创建网络地址相关的，初始化顺序不确定，先给引用
        log.info("RPC "+interfaceClass.getName()+" create success");
        DefaultInvocationHandler defaultInvocationHandler = new DefaultInvocationHandler(interfaceClass.getName());
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, defaultInvocationHandler);
    }
}
