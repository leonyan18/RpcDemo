package com.zust.yan.rpc.net.handler;

import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.Client;
import com.zust.yan.rpc.net.base.DefaultFuture;
import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    private Client client;
    private static volatile Map<Client, AtomicInteger> failTimesMap = new HashMap<>();
    private static volatile Map<Client, AtomicInteger> reTryTimesMap = new HashMap<>();
    private Executor executor = RpcUtils.getExecutor("heartBeat");

    public HeartbeatHandler(Client client) {
        this.client = client;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,
                                   Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            executor.execute(this::doHeartBeat);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    // todo 待优化 改成时间检查不另外创建线程
    private void doHeartBeat() {
        // client 必不为空 初始化
        AtomicInteger failTimes = failTimesMap.get(client);
        AtomicInteger reTryTimes = reTryTimesMap.get(client);
        if (failTimes == null || reTryTimes == null) {
            // 防止多次初始化次数
            synchronized (client) {
                if (failTimesMap.get(client) == null) {
                    failTimes = new AtomicInteger(0);
                    failTimesMap.put(client, failTimes);
                }
                if (reTryTimesMap.get(client) == null) {
                    reTryTimes = new AtomicInteger(0);
                    reTryTimesMap.put(client, reTryTimes);
                }
            }
        }
        DefaultFuture defaultFuture = client.send(Request.makeHeartBeat());
        Response response = defaultFuture.getResBlockInTime();
        // 说明超时
        if (response == null) {
            if (failTimes.incrementAndGet() == RpcUtils.failTimes + 1) {
                try {
                    if (reTryTimes.incrementAndGet() <= RpcUtils.reTryTimes) {
                        // 只在三次5s中时间内没有读写才会执行，说明没有之前没有新请求了，只可能有超时的请求
                        // 此时进行重连
                        client.reConnect();
                    } else {
                        client.close();
                        reTryTimesMap.remove(client);
                        failTimesMap.remove(client);
                        // 直接返回
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                failTimes.set(0);
            }
        } else {
            failTimes.set(0);
            reTryTimes.set(0);
        }

    }

}
