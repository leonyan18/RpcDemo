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

/**
 * @author yan
 */
@Slf4j
public class DefaultInvocationHandler implements InvocationHandler {
    private NetConfigInfo netConfigInfo;
    private Client client;
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
            // 不是第一次说明超时了
            if (client == null) {
                createClient(method.getDeclaringClass().getName(), false);
            }
            RequestMethodInfo methodInfo = new RequestMethodInfo(method, args);
            request = new Request(methodInfo);
            request.setToAddress(client.getInfo().getHost() + ":" + client.getInfo().getPort());
            beforeSend(request);
            Request finalRequest = request;
            DefaultFuture defaultFuture = client.send(request, (r) -> {
                try {
                    afterSend(finalRequest, r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            if (sync) {
                log.info("do sync "+method.getName());
                response = defaultFuture.getResBlockInTime();
                defaultFutures.add(defaultFuture);
                if (response == null) {
                    // 超时尝试新的reponse
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
                log.info("do async "+method.getName());
                response = defaultFuture.getRes();
                // 异步直接返回
                return null;
            }
        }
        // 超时并且多次不行
        return null;
    }

    public NetConfigInfo getNetConfigInfo() {
        return netConfigInfo;
    }

    public void setNetConfigInfo(NetConfigInfo netConfigInfo) {
        this.netConfigInfo = netConfigInfo;
    }

    private void createClient(String clazz, boolean reGet) throws InterruptedException {
        log.info("createClient");
        // 如果获取不到自己获取，初始化顺序可能不一样所以放到这里来懒加载，用的时候在去获取信息
        if (netConfigInfo == null || reGet) {
            netConfigInfo = RpcUtils.getProviderNetInfo(clazz);
        }
        client = new Client(netConfigInfo);
        client.start();
        ClientManager.addClient(client);
    }

    private void beforeSend(Request request) {
        // 设置前一次请求id
        request.setFromRequestId(RpcPathUtils.beforeHandle(request.getRequestId()));
        request.setRequestTime(System.currentTimeMillis());
        MonitorClientUtils.sendToMonitor(new Request(request));
    }

    private void afterSend(Request request, Response response) throws InterruptedException {
        request.setReceiveTime((System.currentTimeMillis()));
        request.setHandleStartTime(response.getHandleStartTime());
        request.setHandleEndTime(response.getHandleEndTime());
        request.setFromAddress(response.getFromAddress());
        RpcPathUtils.afterHandle();
        MonitorClientUtils.sendToMonitor(new Request(request));
        if (!isHot) {
            client.close();
            client = null;
        }
    }
}
