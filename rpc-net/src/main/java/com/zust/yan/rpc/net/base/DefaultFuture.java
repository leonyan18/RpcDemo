package com.zust.yan.rpc.net.base;

import com.zust.yan.rpc.common.utils.RpcUtils;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yan
 */

public class DefaultFuture {
    private static Map<Long, Channel> CHANNELS = new ConcurrentHashMap<Long, Channel>(20);
    private static Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<Long, DefaultFuture>(20);
    private volatile Response res;
    private volatile Request request;
    private Lock lock = new ReentrantLock();
    private Condition receive = lock.newCondition();

    public DefaultFuture(Channel channel, Request request) {
        this.request = request;
        FUTURES.put(request.getRequestId(), this);
        CHANNELS.put(request.getRequestId(), channel);
    }

    public static void handleMsg(Response response) {
        System.out.println(response);
        DefaultFuture defaultFuture = FUTURES.get(response.getRequestId());
        defaultFuture.setRes(response);
        defaultFuture.wakeUpBlock();
    }

    public void wakeUpBlock() {
        lock.lock();
        try {
            receive.signal();
        } finally {
            lock.unlock();
        }
    }

    public Response getRes() {
        return res;
    }

    public void setRes(Response res) {
        this.res = res;
    }

    public Response getResBlock() {
        if (null == res) {
            lock.lock();
            try {
                if (res == null) {
                    receive.await(RpcUtils.timeOut, TimeUnit.MILLISECONDS);
//                    receive.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        return res;
    }
}
