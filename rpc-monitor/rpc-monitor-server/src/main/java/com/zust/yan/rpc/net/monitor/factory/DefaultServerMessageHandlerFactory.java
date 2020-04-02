package com.zust.yan.rpc.net.monitor.factory;

import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.handler.ServerMessageHandlerFactory;
import com.zust.yan.rpc.net.monitor.handler.DefaultMonitorHandler;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.BlockingQueue;

public class DefaultServerMessageHandlerFactory implements ServerMessageHandlerFactory {
    private BlockingQueue queue;

    public DefaultServerMessageHandlerFactory(BlockingQueue<Request> queue) {
        super();
        this.queue = queue;
    }

    @Override
    public SimpleChannelInboundHandler<Object> getHandler() {
        // 非共享方式
        return new DefaultMonitorHandler(queue);
    }
}
