package com.zust.yan.rpc.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class JdkEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(o);
        oos.flush();
        byteBuf.writeBytes(bos.toByteArray());
        bos.close();
        oos.close();
    }
}