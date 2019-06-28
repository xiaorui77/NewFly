package com.newfly.controller;

import com.newfly.common.ConstantDefine;
import com.newfly.pojo.Message;
import com.newfly.service.ChatService;
import com.newfly.service.LobbyService;
import com.newfly.service.LoginService;
import com.newfly.service.MapSceneService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class ServerController
{
    @Autowired
    LobbyService lobbyService;

    @Autowired
    LoginService playerService;

    @Autowired
    ChatService chatPublic;

    @Autowired
    MapSceneService mapService;


    Message handle(ChannelHandlerContext ctx, Message msg) {
        int type = msg.getType();
        switch (type) {
            case ConstantDefine.MESSAGE_PLAYER_LOGIN: // 登录
                return playerService.login(ctx, msg);
            case ConstantDefine.MESSAGE_PLAYER_LOGOUT: // 退出
                return playerService.logout(msg);

            case 2201: // 注册
                return null;
            case 2203: // 注销账号
                return null;

            case ConstantDefine.MESSAGE_CHAT_PUBLIC: // 公共频道聊天
                return chatPublic.chatPublic(msg);
            case ConstantDefine.MESSAGE_CHAT_PRIVATE: // 私聊
                return null;

            case ConstantDefine.MESSAGE_MAP_PLAYER_MOVE: // 玩家移动
                return mapService.movetoPlayer(msg);
            default:
                return new Message(100, "收到了没有");
        }
    }

}// end
