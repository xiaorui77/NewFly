package com.newfly.service;

import com.newfly.common.ConstantDefine;
import com.newfly.common.SocketChannelMap;
import com.newfly.dao.MapSceneRedis;
import com.newfly.dao.PlayerRedis;
import com.newfly.pojo.ResultMessage;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MapSceneService
{
    @Autowired
    PlayerRedis playerRedis;

    @Autowired
    MapSceneRedis mapSceneRedis;


    // 玩家移动
    public ResultMessage movetoPlayer(ResultMessage msg) {
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String sceneId = strings[1];
        String moveX = strings[2];
        String moveY = strings[3];

        // 控制玩家移动
        if (!playerRedis.movetoPlayer(playerId, moveX, moveY))
            return null;

        // 广播给其他玩家
        Set<String> players = mapSceneRedis.sceneMember(sceneId);
        for (String player : players) {
            Channel channel = SocketChannelMap.get(player);
            if (channel == null)
                continue;
            String content = player + ":" + moveX + ":" + moveY;
            ResultMessage message = new ResultMessage(ConstantDefine.MESSAGE_MAP_PLAYER_MOVE_RETURN, content);
            channel.writeAndFlush(message);
        }
        return null;
    }
}
