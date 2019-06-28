package com.newfly.service;

import com.newfly.common.ConstantDefine;
import com.newfly.common.SocketChannelMap;
import com.newfly.dao.ChannelRedis;
import com.newfly.dao.PlayerRedis;
import com.newfly.mapper.PlayerMapper;
import com.newfly.pojo.Message;
import com.newfly.pojo.Player;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService
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
    public Message login(ChannelHandlerContext ctx, Message msg) {
        // 查询mysql数据库
        String[] strings = msg.getBody().split(":");
        String name = strings[0];
        String password = strings[1];
        Player player = new Player(name, password);
        player = playerMapper.login(player);

        if (player == null) {
            return null;
        }
        // 保存该用户的channel
        SocketChannelMap.add(String.valueOf(player.getId()), (SocketChannel) ctx.channel());
        // 将用户信息存入redis
        playerRedis.savePlayer(player);
        return new Message(ConstantDefine.MESSAGE_PLAYER_LOGIN_RETURN, player.toString());
    }

    // 玩家退出
    public Message logout(Message msg) {
        // 判断是否可以退出
        int id = Integer.parseInt(msg.getBody());

        // 删除redis相关字段
        playerRedis.removePlayer(id);
        return null;
    }


}// end
