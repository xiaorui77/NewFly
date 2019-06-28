package com.newfly.service;

import com.newfly.common.ConstantDefine;
import com.newfly.common.SocketChannelMap;
import com.newfly.dao.ChannelRedis;
import com.newfly.dao.PlayerRedis;
import com.newfly.dao.TeamRedis;
import com.newfly.pojo.Message;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ChatService
{
    @Autowired
    PlayerRedis playerRedis;

    @Autowired
    ChannelRedis channelRedis;

    @Autowired
    TeamRedis teamRedis;


    // 频道聊天
    public Message chatPublic(Message msg) {
        String[] strings = msg.getBody().split(":");
        int id = Integer.parseInt(strings[0]);
        int channelId = Integer.parseInt(strings[0]);
        String message = strings[2];

        // 是否有该频道并广播
        if (channelRedis.existChannel(channelId)) {
            Set<String> channelMember = channelRedis.channelMember(channelId);
            for (String curId : channelMember) {
                Channel channel = SocketChannelMap.get(curId);
                Message result = new Message(ConstantDefine.MESSAGE_CHAT_PUBLIC_RETURN, curId + id + message);
                channel.writeAndFlush(result);
            }
        }
        return null;
    }

    // 私聊
    public Message chat(Message msg) {
        String[] strings = msg.getBody().split(":");
        int id = Integer.parseInt(strings[0]);
        int targetId = Integer.parseInt(strings[0]);
        String message = strings[2];

        // 找到目标玩家的channel
        Channel channel = SocketChannelMap.get(String.valueOf(targetId));
        Message result = new Message(ConstantDefine.MESSAGE_CHAT_PRIVATE_RETURN, id + message);
        channel.writeAndFlush(result);

        // 若不在线,存入数据库

        return null;
    }

    // 创建队伍
    public Message createTeam(Message msg) {
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];

        // 是否有队伍
        if (playerRedis.existTeam(playerId)) {
            return null;
        }

        //创建队伍
        int teamId = teamRedis.createTeam(playerId);
        return new Message(ConstantDefine.MESSAGE_TEAM_CREATE_RETURN, String.valueOf(teamId));
    }
}
