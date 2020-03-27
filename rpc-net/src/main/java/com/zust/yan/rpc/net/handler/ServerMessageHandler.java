package com.zust.yan.rpc.net.handler;

import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class ServerMessageHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.writeAndFlush(handlerMessage(msg));
    }
    public abstract Response handlerMessage(Object msg);
}
