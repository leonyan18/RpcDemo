package com.zust.yan.rpc.net;

import com.zust.yan.rpc.net.base.NetConfigInfo;
import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.Response;
import com.zust.yan.rpc.net.base.Server;
import com.zust.yan.rpc.net.handler.KryoDecoder;
import com.zust.yan.rpc.net.handler.KryoEncoder;
import com.zust.yan.rpc.net.handler.ServerHandler;
import com.zust.yan.rpc.net.handler.ServerMessageHandlerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

public class ServerTest {
    public static void main(String[] args) {
        NetConfigInfo info = NetConfigInfo.builder()
                .port(8888)
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
