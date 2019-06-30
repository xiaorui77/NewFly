package com.newfly.service;

import com.newfly.common.ConstantDefine;
import com.newfly.common.SocketChannelMap;
import com.newfly.dao.PlayerRedis;
import com.newfly.dao.SceneRedis;
import com.newfly.dao.TaskRedis;
import com.newfly.mapper.PlayerMapper;
import com.newfly.pojo.Player;
import com.newfly.pojo.ResultMessage;
import com.newfly.pojo.Task;
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
    SceneRedis sceneRedis;

    @Autowired
    TaskRedis taskRedis;


    // 创建新游戏角色
    public void createPlayer() {

    }

    // 删除游戏角色
    public void deletePlayer() {

    }


    // 玩家登录
    public ResultMessage login(ChannelHandlerContext ctx, ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String name = strings[0];
        String password = strings[1];

        Player player = new Player(name, password);

        // 进行登录验证
        player = playerMapper.login(player);
        if (player == null || player.getId() < 10000)
            return null;
        String playerId = String.valueOf(player.getId());

        // 保存该用户的channel
        SocketChannelMap.add(playerId, (SocketChannel) ctx.channel());

        // 将用户信息存入redis
        playerRedis.savePlayer(player);

        // 保存玩家主线任务信息
        Task task = playerMapper.queryTask(playerId);
        taskRedis.saveMainTask(playerId, task);

        // 将玩家加入世界和场景
        sceneRedis.add(String.valueOf(player.getId()), String.valueOf(player.getScene()));

        // 返回玩家信息
        String playerInfo = playerRedis.playerInfo(String.valueOf(player.getId()));
        return new ResultMessage(ConstantDefine.MESSAGE_PLAYER_LOGIN_RETURN, playerInfo);
    }

    // 玩家退出
    public ResultMessage logout(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];

        // 判断是否可以退出
        String sceneId = playerRedis.getScene(playerId);

        // 删除redis相关字段
        playerRedis.removePlayer(playerId);

        // 从世界和scene中移除
        sceneRedis.remove(playerId, sceneId);
        return new ResultMessage(ConstantDefine.MESSAGE_PLAYER_LOGOUT_RETURN, playerId);
    }


}// end
