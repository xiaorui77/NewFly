package com.newfly.service;

import com.newfly.dao.ChannelRedis;
import com.newfly.mapper.PlayerMapper;
import com.newfly.dao.PlayerRedis;
import com.newfly.pojo.Message;
import com.newfly.pojo.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PlayerService
{
    @Autowired
    PlayerMapper playerMapper;

    @Autowired
    PlayerRedis playerRedis;

    @Autowired
    ChannelRedis channelRedis;


    // 创建新玩家
    public void createPlayer() {

    }

    // 删除玩家
    public void deletePlayer() {

    }


    // 玩家登录
    public Message login(Message msg) {
        // 查询mysql数据库
        String[] strings = msg.getBody().split(":");
        String name = strings[0];
        String password = strings[1];
        Player player = new Player(name, password);
        player = playerMapper.login(player);

        if (player == null) {
            return null;
        }
        // 存入redis
        playerRedis.hsetPlayer(player);
        return new Message(3102, player.toString());
    }

    // 玩家退出
    public Message logout(Message msg) {
        // 判断是否可以退出
        int id = Integer.parseInt(msg.getBody());

        // 删除redis相关字段
        playerRedis.hdelPlayer(id);
        return null;
    }

    // 频道聊天
    public Message chatPublic(Message msg) {
        String[] strings = msg.getBody().split(":");
        int id = Integer.parseInt(strings[0]);
        int channelId = Integer.parseInt(strings[0]);
        String message = strings[2];

        // 是否有该频道并广播
        Set<String> channelMember = channelRedis.existChannel();
        for (String channel : channelMember) {

        }
        return null;
    }


    public Message chat(Message msg) {
        String[] strings = msg.getBody().split(":");
        int id = Integer.parseInt(strings[0]);
        int targetId = Integer.parseInt(strings[0]);
        String message = strings[2];

        return null;
    }

}// end
