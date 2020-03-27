package com.zust.yan.rpc.net.handler;

import io.netty.channel.SimpleChannelInboundHandler;

public interface ServerMessageHandlerFactory {
    SimpleChannelInboundHandler<Object>  getHandler();
}
