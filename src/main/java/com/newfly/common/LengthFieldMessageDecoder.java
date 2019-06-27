package com.newfly.common;

import com.newfly.pojo.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.StandardCharsets;

public class LengthFieldMessageDecoder extends LengthFieldBasedFrameDecoder
{
    private static final int MAX_FRAME_LENGTH = 65536; // 最大报文长度
    private static final int LENGTH_FIELD_LENGTH = 2;   // length字段长度
    private static final int LENGTH_FIELD_OFFSET = 0;   // offset
    private static final int LENGTH_ADJUSTMENT = 2;
    private static final int INITIAL_BYTES_TO_STRIP = 2;

    private static final int HEADER_SIZE = 2 + 2;
    private short type;
    private String msgBody;

    public LengthFieldMessageDecoder() {
        super(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in == null) {
            return null;
        }
        if (in.readableBytes() < HEADER_SIZE) {
            throw new Exception("错误的消息");
        }

        /**
         * 通过源码我们能看到在读的过程中
         * 每读一次读过的字节即被抛弃
         * 即指针会往前跳
         */
        int length = in.readShort();
        int type = in.readShort();

        if (in.readableBytes() < length) {
            throw new Exception("消息不正确");
        }

        ByteBuf buf = in.readBytes(length);
        byte[] b = new byte[buf.readableBytes()];
        buf.readBytes(b);

        msgBody = new String(b, StandardCharsets.UTF_8);
        return new Message(length, type, msgBody);
    }
}
