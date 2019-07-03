package com.newfly.controller;

import com.newfly.common.Constant;
import com.newfly.common.RequestType;
import com.newfly.common.SocketChannelMap;
import com.newfly.pojo.Player;
import com.newfly.pojo.ResultMessage;
import com.newfly.pojo.User;
import com.newfly.service.LoginService;
import com.newfly.service.MapSceneService;
import com.newfly.service.PlayerService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Controller
public class LoginController
{
    private static final Logger logger = LoggerFactory.getLogger(NewFlyServer.class);

    @Autowired
    LoginService loginService;

    @Autowired
    PlayerService playerService;


    @Autowired
    MapSceneService mapSceneService;


    // 请求转发
    ResultMessage handle(ChannelHandlerContext ctx, ResultMessage msg) {
        int type = msg.getType();
        Method[] methods = LoginController.class.getDeclaredMethods();
        for (Method method : methods) {
            RequestType requestType = method.getAnnotation(RequestType.class);
            if (requestType == null)
                continue;

            if (type == requestType.value()) {
                try {
                    return (ResultMessage) method.invoke(this, ctx, msg);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        // 该type没有找到
        return null;
    }


    // 用户登录
    @RequestType(Constant.USER_LOGIN)
    private ResultMessage login(ChannelHandlerContext ctx, ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String username = strings[0];
        String password = strings[1];

        // 登录验证
        User user = loginService.LoginValidation(username, password);
        // 登录失败, 后面还要添加其他验证
        if (user == null) {
            return null;
        }

        // 选择角色

        // 由于客户端的原因 这里自动选择默认角色1
        ResultMessage result = selectPlayer(ctx, new ResultMessage(3133, user.getId() + ":" + user.getLastPlayer()));
        result.setType(Constant.USER_LOGIN_RETURN);
        return result;
    }

    // 用户正常退出 同步player数据 未实现
    @RequestType(Constant.MESSAGE_USER_LOGOUT)
    private ResultMessage logout(ChannelHandlerContext ctx, ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String userId = strings[0];

        // 退出player
        Integer lastPlayerId = loginService.queryLastPlayer(Integer.parseInt(userId));
        quitPlayer(ctx, new ResultMessage(Constant.MESSAGE_PLAYER_QUIT, String.valueOf(lastPlayerId)));

        // 其他操作

        return null;
    }


    // 选择player
    @RequestType(Constant.MESSAGE_PLAYER_SELECT)
    private ResultMessage selectPlayer(ChannelHandlerContext ctx, ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String userId = strings[0];
        String charId = strings[1];

        // 选择角色
        Player character = loginService.selectCharacter(Integer.parseInt(userId), Integer.parseInt(charId));
        if (character == null)
            return null;
        loginService.updateLastPlayer(Integer.parseInt(userId), Integer.parseInt(charId));

        // 保存SocketChannel
        SocketChannelMap.add(charId, (SocketChannel) ctx.channel());

        // 保存世界和场景信息
        mapSceneService.addPlayer(charId, String.valueOf(character.getScene()));

        logger.info("用户:" + character.getName() + "(id:" + charId + ")已经登录!");

        // 数据返回
        return new ResultMessage(Constant.MESSAGE_PLAYER_SELECT_RESULT, character.toResult());
    }

    // 退出player 未实现
    @RequestType(Constant.MESSAGE_PLAYER_QUIT)
    private ResultMessage quitPlayer(ChannelHandlerContext ctx, ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];

        // 从redis中读取数据并保存到mysql
        loginService.savePlayerToMysql(playerId);

        // 退出队伍


        // 退出世界场景
        mapSceneService.removePlayer(playerId);

        // 从redis中移除相关字段
        playerService.removePlayer(playerId);

        // 移除player的连接
        SocketChannelMap.remove(playerId);
        logger.info("用户:" + playerId + " 已经退出!");
        return null;
    }

    // 异常退出
    void close(ChannelHandlerContext ctx) {
        String id = SocketChannelMap.findKey(ctx.channel());
        quitPlayer(ctx, new ResultMessage(Constant.MESSAGE_PLAYER_QUIT, id));
    }

}// end
