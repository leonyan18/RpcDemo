package com.zust.yan.rpc.net.base;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.handler.DefalutReceiveHandler;
import com.zust.yan.rpc.net.handler.HeartbeatHandler;
import com.zust.yan.rpc.net.handler.KryoDecoder;
import com.zust.yan.rpc.net.handler.KryoEncoder;
import com.zust.yan.rpc.net.utils.RpcSslContextUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;

import javax.net.ssl.SSLEngine;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yan
 */
public class Client {
    private static AtomicInteger BASE = new AtomicInteger();
    private Integer clientId;
    private NetConfigInfo info;
    private static final int CLOSED = -1;
    private static final int OPENED = 1;
    private static final int NOT_INIT = 0;
    private Integer state = NOT_INIT;
    private Bootstrap bootstrap;

    public ChannelHandler[] handlers() {
        return new ChannelHandler[]{
                new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS),
                new HeartbeatHandler(this),
                new KryoDecoder(),
                new KryoEncoder(),
                new DefalutReceiveHandler()
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
                        ch.pipeline().addLast(handlers());
                    }
                });
        future = bootstrap.connect().sync();
        state = OPENED;
    }

    public void close() throws InterruptedException {
        future.channel().close();
        group.shutdownGracefully().sync();
        state = CLOSED;
    }

    public DefaultFuture send(Request request) {
        future.channel().writeAndFlush(request);
        return new DefaultFuture(future.channel(), request);
    }

    public void reConnect() {
        System.out.println("reconnect " + Thread.currentThread().getName());
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
