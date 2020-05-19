package com.zust.yan.rpc.app.handler;

import com.zust.yan.rpc.app.base.RpcPathUtils;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.RequestMethodInfo;
import com.zust.yan.rpc.net.base.Response;
import com.zust.yan.rpc.net.handler.ServerMessageHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * @author yan
 */
@Slf4j
public class DefaultServerMessageHandler extends ServerMessageHandler {
    private static Map<String, Object> objectMap = new ConcurrentHashMap<>(20);
    private static Executor executor = RpcUtils.getExecutor("ServerMessageHandler");

    @Override
    public void handlerMessage(Request request, ChannelHandlerContext ctx) {
        executor.execute(() -> {
            Response response = new Response();
            InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
            response.setFromAddress(ipSocket.getAddress().getHostAddress() + ":" + ipSocket.getPort());
            response.setToAddress(request.getToAddress());
            response.setRequestId(request.getRequestId());
            long handleTime = System.currentTimeMillis();
            response.setHandleStartTime(handleTime);
            if (request.getData() instanceof RequestMethodInfo) {
                RequestMethodInfo requestMethodInfo = (RequestMethodInfo) request.getData();
                Object object = objectMap.get(requestMethodInfo.getClassName());
                try {
                    Method method = object.getClass().getMethod(requestMethodInfo.getMethodName(), requestMethodInfo.getParameterTypes());
                    method.setAccessible(true);
                    beforeHandle(request.getRequestId(), response);
                    log.info("requestMethodInfo"+(Arrays.asList(requestMethodInfo.getArgs())));
                    Object result = method.invoke(object, requestMethodInfo.getArgs());
                    response.setData(result);
                    afterHandle(response);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            handleTime = System.currentTimeMillis();
            response.setHandleEndTime(handleTime);
            ctx.writeAndFlush(response);
        });
    }

    public static void addHandler(Object object) {
        if (object == null) {
            return;
        }
        // 放入所有接口
        for (Type t : object.getClass().getGenericInterfaces()) {
            objectMap.put(t.getTypeName(), object);
        }
    }

    public static void addHandlers(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        map.forEach((key, value) -> addHandler(value));
    }

    private void beforeHandle(Long requestId, Response response) {
        // 上一次请求id
        response.setFromRequestId(RpcPathUtils.beforeHandle(requestId));
    }

    private void afterHandle(Response response) {
        RpcPathUtils.afterHandle();
    }
}
