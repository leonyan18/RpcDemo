package com.zust.yan.rpc.net.base;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.handler.KryoDecoder;
import com.zust.yan.rpc.net.handler.KryoEncoder;
import com.zust.yan.rpc.net.handler.ServerHandler;
import com.zust.yan.rpc.net.handler.ServerMessageHandlerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

import java.util.concurrent.Executor;

public class Server {
    private static Executor executor = RpcUtils.getExecutor("ServiceExport");
    private NetConfigInfo info;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerMessageHandlerFactory handler;

    public Server() {
    }

    public Server(NetConfigInfo info, ServerMessageHandlerFactory handler) {
        this.info = info;
        this.handler = handler;
    }

    public void startServer() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")
                    .handler(new ServerHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new KryoDecoder());
                            ch.pipeline().addLast(new KryoEncoder());
                            ch.pipeline().addLast(handler.getHandler());
                        }
                    });
            ChannelFuture f = b.bind(info.getPort()).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void start() {
        executor.execute(this::startServer);
    }

    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
