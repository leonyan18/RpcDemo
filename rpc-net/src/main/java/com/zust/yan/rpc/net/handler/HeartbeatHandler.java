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
    private static Map<Client, AtomicInteger> failTimesMap = new HashMap<>();
    private static Map<Client, AtomicInteger> reTryTimesMap = new HashMap<>();
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
        synchronized (client) {
            if (failTimesMap.get(client) == null) {
                failTimesMap.put(client, new AtomicInteger(0));
            }
            if (reTryTimesMap.get(client) == null) {
                reTryTimesMap.put(client, new AtomicInteger(0));
            }
        }
        DefaultFuture defaultFuture = client.send(Request.makeHeartBeat());
        Response response = defaultFuture.getResBlockInTime();
        // 说明超时
        if (response == null) {
            if (failTimesMap.get(client).incrementAndGet() == RpcUtils.failTimes + 1) {
                try {
                    if (reTryTimesMap.get(client).incrementAndGet() <= RpcUtils.reTryTimes) {
                        client.reConnect();
                    } else {
                        client.close();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                failTimesMap.get(client).set(0);
            }
        } else {
            failTimesMap.get(client).set(0);
            reTryTimesMap.get(client).set(0);
        }

    }

}
