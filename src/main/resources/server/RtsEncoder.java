package com.newfly.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


// RtsEncode和RtsDecode重新定义了编码
public class RtsEncoder extends MessageToByteEncoder<RtsProtocal>
{

    @Override
    protected void encode(ChannelHandlerContext ctx, RtsProtocal msg, ByteBuf out) throws Exception {
        //写入消息SmartCar的具体内容
        //1.写入消息的开头的信息标志
        out.writeInt(msg.getHead_data());
        //2.写入协议类型
        out.writeByte(msg.getType());
        //3.写入协议号
        out.writeInt(msg.getProtocloNumber());
        //4.写入消息的长度(此长度不包含，head_data 和 contentLength所占的字节)
        out.writeInt(msg.getContentLength());
        //5.写入消息的内容
        out.writeBytes(msg.getContent());
    }
}
