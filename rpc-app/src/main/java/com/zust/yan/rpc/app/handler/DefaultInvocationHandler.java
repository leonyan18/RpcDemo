package com.zust.yan.rpc.app.handler;

import com.zust.yan.rpc.app.base.RpcPathUtils;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.base.*;
import com.zust.yan.rpc.net.monitor.utils.MonitorClientUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author yan
 */
@Slf4j
public class DefaultInvocationHandler implements InvocationHandler {
    private NetConfigInfo netConfigInfo;
    private Client client;
    private Boolean isHot;

    public DefaultInvocationHandler() {
        isHot = true;
    }

    public DefaultInvocationHandler(Boolean isHot) {
        this.isHot = isHot;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (client == null) {
            createClient();
        }
        RequestMethodInfo methodInfo = new RequestMethodInfo(method, args);
        Request request = new Request(methodInfo);
        beforeSend(request);
        DefaultFuture defaultFuture = client.send(request);
        Response response = defaultFuture.getResBlock();
        afterSend(request, response);
        return response.getData();
    }

    public NetConfigInfo getNetConfigInfo() {
        return netConfigInfo;
    }

    public void setNetConfigInfo(NetConfigInfo netConfigInfo) {
        this.netConfigInfo = netConfigInfo;
    }

    private void createClient() throws InterruptedException {
        log.info("createClient");
        NetConfigInfo info = NetConfigInfo.builder()
                .address("127.0.0.1")
                .port(8888)
                .build();
        client = new Client(info);
        client.start();
    }

    private void beforeSend(Request request) {
        RpcPathUtils.beforeHandle(request.getRequestId());
        request.setRequestTime(System.currentTimeMillis());
    }

    private void afterSend(Request request, Response response) throws InterruptedException {
        request.setReceiveTime((System.currentTimeMillis()));
        request.setHandleStartTime(response.getHandleStartTime());
        request.setHandleEndTime(response.getHandleEndTime());
        request.setToRequestId(response.getToRequestId());
        MonitorClientUtils.sendToMonitor(request);
        if (!isHot) {
            client.close();
            client = null;
        }
    }
}
