package com.zust.yan.rpc.net;

import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MonitorHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Request) {
            System.out.println("read");
            Request request = (Request) msg;
            if (request.getType() != 0) {
                ctx.writeAndFlush(new Response(request.getRequestId(), "test"));
            }else{
                ctx.writeAndFlush(Response.makeHeartBeat(request.getRequestId()));
            }
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}
