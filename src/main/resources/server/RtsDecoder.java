package com.newfly.server;

import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

// 自定义解码器
public class RtsDecoder extends ByteToMessageDecoder
{

    /**
     * 消息头，协议开始的标志 head_data ,int类型，占4个字节
     * 数据的长度contentLength,int类型，占4个字节
     * 数据的类型type,byte类型,占1个字节
     */
    private final int BASE_LENGTH = 4 + 4 + 1;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        //可读长度必须大于基本长度
        if (in.readableBytes() >= BASE_LENGTH) {
            //防止socket字节流攻击
            //防止客户端传过来的数据过大，因为太大的数据，是不合理的
            if (in.readableBytes() > 2048) {
                in.skipBytes(in.readableBytes());
            }

            //记录包头开始的index
            int beginReader;
            while (true) {
                //获取包头开始的index
                beginReader = in.readerIndex();
                //标记包头开始的index
                in.markReaderIndex();

                //读到了协议开始的标志，结束while循环
                if (in.readInt() == ConstantValue.HEAD_DATA) {
                    break;
                }

                //如果未读到包头略过一个字节去读包头，去读取是否是消息的开头
                in.resetReaderIndex();
                in.readByte();

                //如果这时候包的长度不足 最低要求，return
                if (in.readableBytes() < BASE_LENGTH) {
                    return;
                }
            }

            //获取协议类型
            byte type = in.readByte();

            //如果type == 2表示是心跳协议，心跳协议没有协议号和协议体
            if (type == 2) {
                SmartCarProtocal protocal = new SmartCarProtocal(type, 0, 0, new byte[]{});
                out.add(protocal);
                return;
            }

            //协议号
            int protocloNumber = in.readInt();
            //消息的长度
            int length = in.readInt();
            //判断请求数据包数据是否到齐
            if (in.readableBytes() < length) {
                //消息未到齐,还远读指针
                in.readerIndex(beginReader);
                return;
            }

            //读取data数据
            byte[] data = new byte[length];
            in.readBytes(data);

            SmartCarProtocal protocal = new SmartCarProtocal(type, protocloNumber, data.length, data);
            out.add(protocal);
        }
    }
}
