package com.newfly.service;

import com.newfly.common.ConstantDefine;
import com.newfly.common.SocketChannelMap;
import com.newfly.dao.MapSceneRedis;
import com.newfly.dao.PlayerRedis;
import com.newfly.dao.TeamRedis;
import com.newfly.mapper.MessageMapper;
import com.newfly.pojo.Message;
import com.newfly.pojo.ResultMessage;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


/*
 * 广播频道
 * 0: 无
 * 1: 世界
 * 2: 当前scene
 * 3: 附近
 * 4: 当前家族
 * 5: 当前队伍
 * */
@Service
public class ChatService
{
    @Autowired
    MessageMapper messageMapper;

    @Autowired
    PlayerRedis playerRedis;

    @Autowired
    MapSceneRedis mapSceneRedis;

    @Autowired
    TeamRedis teamRedis;


    // 广播聊天
    public ResultMessage chatPublic(ResultMessage msg) {
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String channelId = strings[1];
        String message = strings[2];

        Set<String> players = null;
        // 查找频道的玩家列表
        switch (Integer.parseInt(channelId)) {
            case 1: // 世界频道
                players = mapSceneRedis.worldMember();
                break;
            case 2: // 当前scene
                players = mapSceneRedis.sceneMember(channelId);
                break;
            case 3: // 附近
                break;
            case 4: // 家族
                break;
            case 5: // 队伍
                players = teamRedis.teamMember(channelId);
                break;
        }

        if (players == null)
            return null;
        // 给其他所有人广播,
        players.remove(playerId);
        for (String curId : players) {
            Channel channel = SocketChannelMap.get(curId);
            ResultMessage result = new ResultMessage(ConstantDefine.MESSAGE_CHAT_PUBLIC_RETURN, curId + ":" + playerId + ":" + message);
            channel.writeAndFlush(result);
        }
        return null;
    }

    // 好友聊天
    public ResultMessage chat(ResultMessage msg) {
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String targetId = strings[1];
        String message = strings[2];

        // 找到目标玩家的channel
        Channel channel = SocketChannelMap.get(targetId);
        ResultMessage result = new ResultMessage(ConstantDefine.MESSAGE_CHAT_PRIVATE_RETURN, playerId + ":" + message);

        // 在线:直接发送; 不在线,存入数据库
        if (channel != null) {
            channel.writeAndFlush(result);
        } else {
            Message m = new Message(Integer.parseInt(playerId), Integer.parseInt(targetId), message);
            messageMapper.save(m);
        }
        return null;
    }

}// end
