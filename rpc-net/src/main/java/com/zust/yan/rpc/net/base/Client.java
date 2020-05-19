package com.zust.yan.rpc.net.base;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.handler.DefalutReceiveHandler;
import com.zust.yan.rpc.net.handler.HeartbeatHandler;
import com.zust.yan.rpc.net.handler.KryoDecoder;
import com.zust.yan.rpc.net.handler.KryoEncoder;
import com.zust.yan.rpc.net.utils.RpcSslContextUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLEngine;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yan
 */
@Slf4j
public class Client {
    private static AtomicInteger BASE = new AtomicInteger();
    private Integer clientId;
    private NetConfigInfo info;
    private static final int CLOSED = -1;
    private static final int OPENED = 1;
    private static final int NOT_INIT = 0;
    private Integer state = NOT_INIT;
    private Bootstrap bootstrap;
    private DefalutReceiveHandler defalutReceiveHandler;

    public ChannelHandler[] handlers() {
        defalutReceiveHandler = new DefalutReceiveHandler();
        return new ChannelHandler[]{
                new IdleStateHandler(0, 0, 50, TimeUnit.SECONDS),
                new HeartbeatHandler(this),
                new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())),
                new ObjectEncoder(),
//                new KryoDecoder(),
//                new KryoEncoder(),
                defalutReceiveHandler
        };

    }

    private ChannelFuture future;
    private EventLoopGroup group;


    public Client(NetConfigInfo info) {
        clientId = BASE.incrementAndGet();
        state = 0;
        this.info = info;
    }

    public void setInfo(NetConfigInfo info) {
        this.info = info;
    }

    public NetConfigInfo getInfo() {
        return info;
    }

    public void start() throws InterruptedException {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(info.getHost(), info.getPort()))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        if (RpcSslContextUtils.getClientSslContext() != null) {
                            SSLEngine engine = RpcSslContextUtils.getClientSslContext().newEngine(ch.alloc());
                            engine.setUseClientMode(true);
                            engine.setEnabledProtocols(engine.getSupportedProtocols());
                            engine.setEnabledCipherSuites(engine.getSupportedCipherSuites());
                            // 客户端写true
                            ch.pipeline().addFirst("ssl", new SslHandler(engine, true));
                        }
//                        ch.pipeline().addFirst(new LoggingHandler());
                        ch.pipeline().addLast(handlers());
                    }
                });
        future = bootstrap.connect().sync();
        state = OPENED;
    }

    public void close() throws InterruptedException {
        defalutReceiveHandler.ctx.channel().close();
        group.shutdownGracefully().sync();
        state = CLOSED;
    }

    public DefaultFuture send(Request request) {
        defalutReceiveHandler.ctx.writeAndFlush(request);
        return new DefaultFuture(future.channel(), request);
    }

    public DefaultFuture send(Request request, CallBack callBack) {
        DefaultFuture defaultFuture = new DefaultFuture(future.channel(), request, callBack);
        defalutReceiveHandler.ctx.writeAndFlush(request).addListener((ChannelFutureListener) future -> log.info("Request send done request" + request));
        return defaultFuture;
    }

    public void reConnect() {
        // 防止多次重连
        synchronized (info) {
            try {
                close();
                start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean isClosed() {
        return state.equals(CLOSED);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        Client client = (Client) o;
        return clientId.equals(client.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }
}
