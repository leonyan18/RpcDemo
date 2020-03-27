package com.zust.yan.rpc.net.handler;

import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class ServerMessageHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        handlerMessage(msg,ctx.channel());
    }
    public abstract void handlerMessage(Object msg, Channel channel);
}
