package com.zust.yan.rpc.net.handler;

import com.zust.yan.rpc.net.base.Client;
import com.zust.yan.rpc.net.base.DefaultFuture;
import com.zust.yan.rpc.net.base.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DefalutReceiveHandler extends SimpleChannelInboundHandler<Object> {
    private Client client;

    public DefalutReceiveHandler(Client client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg != null) {
            if (msg instanceof Response) {
                DefaultFuture.handleMsg((Response) msg);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        client.setActiveCtx(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
