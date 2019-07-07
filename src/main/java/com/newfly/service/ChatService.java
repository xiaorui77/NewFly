package com.newfly.service;

import com.newfly.common.Constant;
import com.newfly.common.SocketChannelMap;
import com.newfly.dao.PlayerRedis;
import com.newfly.dao.SceneRedis;
import com.newfly.dao.TeamRedis;
import com.newfly.mapper.MessageMapper;
import com.newfly.pojo.Message;
import com.newfly.pojo.ResultMessage;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
    SceneRedis sceneRedis;

    @Autowired
    TeamRedis teamRedis;

    @Autowired
    JedisPool jedisPool;


    // 添加到消息队列
    public void addMQ(String message) {
        Jedis jedis = jedisPool.getResource();
        jedis.rpush("chat_public",message);
        jedis.close();
    }


    // 广播聊天
    public void chatPublic(String playerId, String channelId, String message) {
        Set<String> players = null;
        // 查找频道的玩家列表
        switch (Integer.parseInt(channelId)) {
            case 1: // 世界频道
                players = sceneRedis.worldMember();
                break;
            case 2: // 当前scene
                // 获取该玩家所在的sceneId
                String sceneId = playerRedis.getScene(playerId);
                players = sceneRedis.sceneMember(sceneId);
                break;
            case 3: // 附近
                break;
            case 4: // 家族
                break;
            case 5: // 队伍
                // 获取该玩家所在的sceneId
                String teamId = playerRedis.getTeam(playerId);
                players = teamRedis.teamMember(teamId);
                break;
        }

        if (players == null)
            return;
        // 发送者的name
        String sendName = playerRedis.getName(playerId);
        // 给其他所有人广播
        players.remove(playerId);
        for (String curId : players) {
            String content = channelId + ":" + playerId + ":" + sendName + ":" + message;
            SocketChannelMap.sendTo(curId, Constant.MESSAGE_CHAT_PUBLIC_RETURN, content);
        }
        return;
    }

    // 好友聊天
    public ResultMessage chat(String playerId, String targetId, String message) {

        // 找到目标玩家的channel
        Channel channel = SocketChannelMap.get(targetId);
        ResultMessage result = new ResultMessage(Constant.MESSAGE_CHAT_PRIVATE_RETURN, playerId + ":" + message);

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
