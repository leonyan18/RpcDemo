package com.zust.yan.rpc.net.monitor.handler;

import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.RequestMethodInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

/**
 * @author yan
 */
@Slf4j
public class DefaultMonitorHandler extends SimpleChannelInboundHandler<Object> {
    private BlockingQueue<Request> blockingQueue;

    public DefaultMonitorHandler(BlockingQueue<Request> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Request) {
            Object object = ((Request) msg).getData();
            // 避免阻塞io线程
            if (object instanceof RequestMethodInfo) {
                try {
                    log.info("put msg to queue");
                    blockingQueue.put((Request) msg);
                } catch (InterruptedException e) {
                    log.error("blockingQueue put error msg=" + msg);
                    e.printStackTrace();
                }
            }
        }
        ctx.flush();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught " + ctx);
        cause.printStackTrace();
    }
}
