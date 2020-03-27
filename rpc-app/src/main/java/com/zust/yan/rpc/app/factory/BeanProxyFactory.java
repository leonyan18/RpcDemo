package com.zust.yan.rpc.app.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author yan
 */
public class BeanProxyFactory {
    public static <T> T createProxy(Class<T> interfaceClass, InvocationHandler invocationHandler) {
        T proxy = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, invocationHandler);
        return proxy;
    }
}
