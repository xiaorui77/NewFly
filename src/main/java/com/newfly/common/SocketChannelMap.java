package com.newfly.common;

import com.newfly.pojo.ResultMessage;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SocketChannelMap
{
    private static Map<String, SocketChannel> map = new ConcurrentHashMap<>();


    public static void add(String clientId, SocketChannel socketChannel) {
        map.put(clientId, socketChannel);
    }

    public static Channel get(String clientId) {
        return map.get(clientId);
    }

    public static void remove(SocketChannel socketChannel) {
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() == socketChannel) {
                map.remove(entry.getKey());
            }
        }
    }


    // 给指定人发送消息
    public static boolean sendTo(String clientId, int type, String content) {
        if (content == null)
            return false;

        Channel channel = map.get(clientId);
        if (channel == null)
            return false;

        channel.writeAndFlush(new ResultMessage(type, content));
        return true;
    }

    // 给多个人发送消息
    public static boolean sendAll(Set<String> all, int type, String content) {
        if (content == null)
            return false;

        for (String a : all) {
            Channel channel = map.get(a);
            if (channel == null)
                break;
            channel.writeAndFlush(new ResultMessage(type, content));
        }
        return true;
    }

}// end
