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
    // 由于没请求的id不一样，不存在收到不同response的情况所以不需要生成每个不同的代理类
    public static Map<NetConfigInfo, Object> proxyMap = new ConcurrentHashMap<>(20);

    public static <T> T createProxy(Class<T> interfaceClass) {
        NetConfigInfo netConfigInfo = RpcUtils.getProviderNetInfo(interfaceClass.getName());
        T proxy = (T) proxyMap.get(netConfigInfo);
        if (proxy == null) {
            // object和defaultInvocationHandler 一对一
            DefaultInvocationHandler defaultInvocationHandler = new DefaultInvocationHandler();
            defaultInvocationHandler.setNetConfigInfo(netConfigInfo);
            proxy = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                    new Class<?>[]{interfaceClass}, defaultInvocationHandler);
        }
        return proxy;
    }
}
