package com.zust.yan.rpc.net.handler;

import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerMessageHandler1 extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
        Request request= (Request) msg;
        ctx.writeAndFlush(new Response(request.getRequestId(),"test"));
    }
}
