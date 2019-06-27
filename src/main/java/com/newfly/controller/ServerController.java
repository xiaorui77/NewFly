package com.newfly.controller;

import com.newfly.service.LobbyService;
import com.newfly.pojo.Message;
import com.newfly.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class ServerController
{
    @Autowired
    LobbyService lobbyService;

    @Autowired
    PlayerService playerService;


    Message handle(Message msg) {
        short type = msg.getType();
        switch (type) {
            case 2101: // 登录
                return playerService.login(msg);
            case 2103: // 退出
                return playerService.logout(msg);
            case 2201: // 注册
            case 2203: // 注销账号
            case 3211: // 公共频道聊天
                return playerService.chatPublic(msg);
            default:
                return new Message(100, "收到了没有");
        }
    }

}// end
