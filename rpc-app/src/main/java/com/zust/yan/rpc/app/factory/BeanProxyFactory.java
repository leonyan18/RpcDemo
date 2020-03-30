package com.zust.yan.rpc.app.factory;

import com.zust.yan.rpc.app.handler.DefaultInvocationHandler;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.DefaultFuture;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yan
 */
public class BeanProxyFactory {

    public static <T> T createProxy(Class<T> interfaceClass) {
        // 先不创建网络地址相关的，初始化顺序不确定，先给引用
        DefaultInvocationHandler defaultInvocationHandler = new DefaultInvocationHandler();
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, defaultInvocationHandler);
    }
}
