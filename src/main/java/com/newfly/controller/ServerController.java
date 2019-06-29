package com.newfly.controller;

import com.newfly.common.ConstantDefine;
import com.newfly.pojo.ResultMessage;
import com.newfly.service.*;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Controller;


@Controller
public class ServerController
{
    private final LobbyService lobbyService;

    private final LoginService playerService;

    private final ChatService chatPublic;

    private final MapSceneService mapService;

    private final TeamService teamService;

    public ServerController(LobbyService lobbyService, LoginService playerService, ChatService chatPublic, MapSceneService mapService, TeamService teamService) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
        this.chatPublic = chatPublic;
        this.mapService = mapService;
        this.teamService = teamService;
    }


    ResultMessage handle(ChannelHandlerContext ctx, ResultMessage msg) {
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
            case ConstantDefine.MESSAGE_CHAT_PRIVATE: // 好友聊天
                return chatPublic.chat(msg);

            case ConstantDefine.MESSAGE_TEAM_CREATE: // 创建队伍
                return teamService.createTeam(msg);
            case ConstantDefine.MESSAGE_TEAM_QUERY: // 查询队伍
                return teamService.queryTeam(msg);
            case ConstantDefine.MESSAGE_TEAM_JOIN: // 加入队伍
                return teamService.joinTeam(msg);
            case ConstantDefine.MESSAGE_TEAM_QUIT: // 离开队伍
                return teamService.quitTeam(msg);

            case ConstantDefine.MESSAGE_MAP_PLAYER_MOVE: // 玩家移动
                return mapService.movetoPlayer(msg);
            default:
                return new ResultMessage(100, "收到了没有");
        }
    }

}// end
