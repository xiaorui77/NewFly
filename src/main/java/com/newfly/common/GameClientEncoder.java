package com.newfly.common;

import com.newfly.pojo.ResultMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class GameClientEncoder extends MessageToByteEncoder<ResultMessage>
{
    @Override
    protected void encode(ChannelHandlerContext ctx, ResultMessage msg, ByteBuf out) throws Exception {
        if (msg == null)
            return;
        String message = String.valueOf(msg.getType()) + ":" + msg.getBody();
        byte[] bytes = message.getBytes(CharsetUtil.UTF_8);
        out.writeBytes(bytes);
    }
}
