package com.zust.yan.rpc.net.base;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.handler.*;
import com.zust.yan.rpc.net.utils.RpcSslContextUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

import javax.net.ssl.SSLEngine;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class Server {
    private static Executor executor = RpcUtils.getExecutor("ServiceExport");
    private NetConfigInfo info;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerMessageHandlerFactory handler;
    private static final int CLOSED = -1;
    private static final int OPENED = 1;
    private static final int NOT_INIT = 0;
    private Integer state = NOT_INIT;
    private  ChannelFuture future;

    public ChannelHandler[] handlers() {
        return new ChannelHandler[]{
//                new IdleStateHandler(0, 0, 50, TimeUnit.SECONDS),
                new AcceptorIdleStateTrigger(),
                new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())),
                new ObjectEncoder(),
//                new KryoDecoder(),
//                new KryoEncoder(),
                handler.getHandler()
        };

    }

    public Server() {
    }

    public Server(NetConfigInfo info, ServerMessageHandlerFactory handler) {
        this.info = info;
        this.handler = handler;
    }

    public void startServer() {
        bossGroup = new NioEventLoopGroup();
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
                            // ssl配置
                            if (RpcSslContextUtils.getServerSslContext() != null) {
                                SSLEngine engine = RpcSslContextUtils.getServerSslContext().newEngine(ch.alloc());
                                engine.setUseClientMode(false);
                                engine.setEnabledProtocols(engine.getSupportedProtocols());
                                engine.setEnabledCipherSuites(engine.getSupportedCipherSuites());
                                ch.pipeline().addFirst("ssl", new SslHandler(engine, false));
                            }
//                            ch.pipeline().addFirst(new LoggingHandler());
                            ch.pipeline().addLast(handlers());
                        }
                    });
            future = b.bind(info.getPort()).sync();
            state = OPENED;
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            state = CLOSED;
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
