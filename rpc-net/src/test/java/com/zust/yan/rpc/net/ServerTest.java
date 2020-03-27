package com.zust.yan.rpc.net;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.Response;
import com.zust.yan.rpc.net.base.Server;
import io.netty.channel.*;

public class ServerTest {
    public static void main(String[] args) {
        NetConfigInfo info = NetConfigInfo.builder()
                .port(8887)
                .build();
        Server server=new Server(info, () -> new SimpleChannelInboundHandler<Object>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println(msg);
                Request request= (Request) msg;
                ctx.writeAndFlush(new Response(request.getRequestId(),"test"));
            }
        });
        server.start();
    }
}
