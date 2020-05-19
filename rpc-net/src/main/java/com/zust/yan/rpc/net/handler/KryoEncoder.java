package com.zust.yan.rpc.net.handler;

import com.zust.yan.rpc.net.config.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class KryoEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf out) throws Exception {
        KryoSerializer.serialize(message, out);
        ctx.flush();
    }

}