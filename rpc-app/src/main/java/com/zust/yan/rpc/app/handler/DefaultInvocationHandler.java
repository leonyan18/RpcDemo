package com.zust.yan.rpc.app.handler;

import com.zust.yan.rpc.app.base.RpcPathUtils;
import com.zust.yan.rpc.app.manage.ClientManager;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.*;
import com.zust.yan.rpc.net.monitor.utils.MonitorClientUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yan
 */
@Slf4j
public class DefaultInvocationHandler implements InvocationHandler {
    private Map<NetConfigInfo, Client> clientMap = new ConcurrentHashMap<>();
    private Boolean isHot;
    private String targetClazzName;
    private Boolean sync;

    public DefaultInvocationHandler() {
        isHot = true;
    }

    public DefaultInvocationHandler(String clazzName, Boolean sync) {
        isHot = true;
        targetClazzName = clazzName;
        this.sync = sync;
    }

    public DefaultInvocationHandler(Boolean isHot) {
        this.isHot = isHot;
    }

    public DefaultInvocationHandler(String clazzName, Boolean isHot, Boolean sync) {
        this.isHot = isHot;
        targetClazzName = clazzName;
        this.sync = sync;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = null;
        Response response = null;
        List<DefaultFuture> defaultFutures = new ArrayList<>();
        int times = 0;
        while (times++ < RpcUtils.reTryTimes) {
            Client client = createClient(method.getDeclaringClass().getName());
            RequestMethodInfo methodInfo = new RequestMethodInfo(method, args);
            request = new Request(methodInfo);
            request.setToAddress(client.getInfo().getHost() + ":" + client.getInfo().getPort());
            // 发送前处理
            beforeSend(request);
            Request finalRequest = request;
            DefaultFuture defaultFuture = client.send(request, (r) -> {
                try {
                    // 发送后处理
                    afterSend(finalRequest, r, client);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            if (sync) {
                response = defaultFuture.getResBlockInTime();
                defaultFutures.add(defaultFuture);
                if (response == null) {
                    client.responseOutTime();
                    // 超时尝试新的response
                    // 并检查之前的response是否成功
                    for (DefaultFuture future : defaultFutures) {
                        // 这里非阻塞
                        if (future.getRes() != null) {
                            return future.getRes().getData();
                        }
                    }
                } else {
                    // 没超时就直接返回
                    return response.getData();
                }
            } else {
                // 异步直接返回
                return null;
            }
        }
        // 超时并且多次不行
        return null;
    }

    private Client createClient(String clazz) throws InterruptedException {
        // 负载均衡获取对应的网络地址
        NetConfigInfo netConfigInfo = RpcUtils.getProviderNetInfo(clazz);
        Client client = null;
        if ((client = clientMap.get(netConfigInfo)) != null) {
            return client;
        }
        // 创建新连接
        client = new Client(netConfigInfo);
        client.start();
        clientMap.put(netConfigInfo, client);
        ClientManager.addClient(client);
        return client;
    }

    private void beforeSend(Request request) {
        // 设置前一次请求id
        request.setFromRequestId(RpcPathUtils.beforeHandle(request.getRequestId()));
        request.setRequestTime(System.currentTimeMillis());
        MonitorClientUtils.sendToMonitor(new Request(request));
    }

    private void afterSend(Request request, Response response, Client client) throws InterruptedException {
        request.setReceiveTime((System.currentTimeMillis()));
        request.setHandleStartTime(response.getHandleStartTime());
        request.setHandleEndTime(response.getHandleEndTime());
        request.setFromAddress(response.getFromAddress());
        RpcPathUtils.afterHandle();
        MonitorClientUtils.sendToMonitor(new Request(request));
        if (!isHot) {
            client.close();
        }
    }
}
