package com.zust.yan.rpc.net.base;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.handler.DefalutReceiveHandler;
import com.zust.yan.rpc.net.handler.KryoDecoder;
import com.zust.yan.rpc.net.handler.KryoEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private NetConfigInfo info;
    private static final int CLOSED=-1;
    private static final int OPEND=1;
    private static final int INITAL=1;
    private Integer state=0;
    private ChannelFuture future;
    private EventLoopGroup group;
    private List<ChannelHandler> channelHandler;

    public Client() {
        channelHandler=new ArrayList<ChannelHandler>(5);
    }

    public Client(NetConfigInfo info) {
        state=0;
        channelHandler=new ArrayList<ChannelHandler>(5);
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
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(info.getAddress(), info.getPort()))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ch.pipeline().addLast(new KryoDecoder());
                        ch.pipeline().addLast(new KryoEncoder());
                        ch.pipeline().addLast(new DefalutReceiveHandler());
                    }
                });
        future = b.connect().sync();
        state=1;
    }

    public void close() throws InterruptedException {
        future.channel().close();
        group.shutdownGracefully().sync();
        state=-1;
    }

    public DefaultFuture send(Request request){
        future.channel().writeAndFlush(request);
        return new DefaultFuture(future.channel(),request);
    }
    public Boolean isClosed(){
        return state.equals(CLOSED);
    }
}
