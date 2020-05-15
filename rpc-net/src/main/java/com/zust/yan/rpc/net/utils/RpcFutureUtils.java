package com.zust.yan.rpc.net.utils;

import com.zust.yan.rpc.net.base.CallBack;
import com.zust.yan.rpc.net.base.DefaultFuture;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yan
 */
public class RpcFutureUtils {
    private static Map<Long, Channel> CHANNELS = new ConcurrentHashMap<>(20);
    private static Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<>(20);
    private static Map<Long, CallBack> CALLBACKS = new ConcurrentHashMap<>(20);
    /**
     * 相同线程操作同一个val
     */
    private static Map<Thread, List<DefaultFuture>> FUTUREIDS = new ConcurrentHashMap<>(20);

    public static void putChannel(Long id, Channel channel) {
        CHANNELS.put(id, channel);
    }

    public static void putDefaultFuture(Long id, DefaultFuture defaultFuture,Boolean isNormal) {
        FUTURES.put(id, defaultFuture);
        if(isNormal){
            // 只有普通的才会有异步同步之分
            Thread thread = Thread.currentThread();
            FUTUREIDS.computeIfAbsent(thread, k -> new ArrayList<>());
            FUTUREIDS.get(thread).add(defaultFuture);
        }


    }

    public static void putCallBack(Long id, CallBack callBack) {
        CALLBACKS.put(id, callBack);
    }

    public static Channel getChannel(Long id) {
        return CHANNELS.get(id);
    }

    public static DefaultFuture getDefaultFuture(Long id) {
        return FUTURES.get(id);
    }

    public static CallBack getCallBack(Long id) {
        return CALLBACKS.get(id);
    }

    public static List<DefaultFuture> getFutures() {
        List<DefaultFuture> futures = FUTUREIDS.get(Thread.currentThread());
        // 删除避免map过大
        FUTUREIDS.remove(Thread.currentThread());
        return futures;
    }

    /**
     * @param requestId 请求id
     *                  一般情况只有一个线程请求相同id
     */
    public static void removeFuture(Long requestId) {
        FUTURES.remove(requestId);
        CHANNELS.remove(requestId);
        CALLBACKS.remove(requestId);
    }
}
