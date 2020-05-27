package com.zust.yan.rpc.net.base;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.handler.DefalutReceiveHandler;
import com.zust.yan.rpc.net.handler.HeartbeatHandler;
import com.zust.yan.rpc.net.utils.RpcSslContextUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
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
    private volatile NetConfigInfo info;
    private static final int CLOSED = -1;
    private static final int OPENED = 1;
    private static final int NOT_INIT = 0;
    /**
     * 状态标志
     */
    private volatile Integer state = NOT_INIT;
    private volatile ChannelHandlerContext ctx;
    private Bootstrap bootstrap;

    public ChannelHandler[] handlers() {
        return new ChannelHandler[]{
                new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS),
                new HeartbeatHandler(this),
                new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())),
                new ObjectEncoder(),
//                new KryoDecoder(),
//                new KryoEncoder(),
                new DefalutReceiveHandler(this)
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
    }

    public void close() throws InterruptedException {
        ctx.channel().close();
        group.shutdownGracefully().sync();
        state = CLOSED;
    }

    public DefaultFuture send(Request request) {
        return send(request, null);
    }

    public DefaultFuture send(Request request, CallBack callBack) {
        if (state == CLOSED) {
            return DefaultFuture.getClosedDefaultFuture(request);
        }
        while (state == NOT_INIT) {
            Thread.yield();
        }
        DefaultFuture defaultFuture = new DefaultFuture(future.channel(), request, callBack);
        ctx.writeAndFlush(request);
        return defaultFuture;
    }

    public void responseOutTime() {
        info.getFailTimes().increment();
    }

    public void reConnect() {
        // 防止多次重连
        synchronized (info) {
            try {
                // 必须首先将标志置为未初始化
                // 防止后续请求发送在重连之前发送，造成数据丢失
                state=NOT_INIT;
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

    public void setActiveCtx(ChannelHandlerContext ctx) {
        // 顺序很重要
        this.ctx = ctx;
        this.state = OPENED;
    }
}
