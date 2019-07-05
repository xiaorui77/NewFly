package com.newfly.common;

import com.newfly.controller.NewFlyServer;
import com.newfly.pojo.ResultMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameClientEncoder extends MessageToByteEncoder<ResultMessage>
{
    private static final Logger logger = LoggerFactory.getLogger(NewFlyServer.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ResultMessage msg, ByteBuf out) throws Exception {
        if (msg == null || msg.getBody() == null)
            return;
        logger.info("发送" + msg.getType() + "类型的数据=>" + msg.getBody());
        String message = msg.getType() + ":" + msg.getBody() + ":";
        byte[] bytes = message.getBytes(CharsetUtil.UTF_8);
        out.writeBytes(bytes);
    }
}
