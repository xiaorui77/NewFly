package com.newfly.controller;

import com.newfly.common.Constant;
import com.newfly.pojo.ResultMessage;
import com.newfly.service.*;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Controller;


@Controller
public class ServerController
{

    private final PlayerService playerService;

    private final ChatService chatPublic;

    private final MapSceneService mapService;

    private final TeamService teamService;

    private final TaskService taskService;

    public ServerController( PlayerService playerService, ChatService chatPublic, MapSceneService mapService, TeamService teamService, TaskService taskService) {
        this.playerService = playerService;
        this.chatPublic = chatPublic;
        this.mapService = mapService;
        this.teamService = teamService;
        this.taskService = taskService;
    }


    ResultMessage handle(ChannelHandlerContext ctx, ResultMessage msg) {
        int type = msg.getType();
        switch (type) {

            case Constant.MESSAGE_PLAYER_QUERY: // 查询所有玩家信息
                return playerService.queryPlayer(msg);

            case Constant.MESSAGE_CHAT_PUBLIC: // 公共频道聊天
                return chatPublic.chatPublic(msg);
            case Constant.MESSAGE_CHAT_PRIVATE: // 好友聊天
                return chatPublic.chat(msg);

            case Constant.MESSAGE_TEAM_CREATE: // 创建队伍
                return teamService.createTeam(msg);
            case Constant.MESSAGE_TEAM_QUERY: // 查询队伍
                return teamService.queryTeam(msg);
            case Constant.MESSAGE_TEAM_JOIN: // 加入队伍
                return teamService.joinTeam(msg);
            case Constant.MESSAGE_TEAM_QUIT: // 离开队伍
                return teamService.quitTeam(msg);

            case Constant.MESSAGE_MAP_PLAYER_MOVE: // 玩家移动
                return mapService.movetoPlayer(msg);

            case Constant.MESSAGE_TASK_CHANGE: // 任务阶段变化
                return taskService.change(msg);
            case Constant.MESSAGE_TASK_QUERY: // 查询任务
                return taskService.checkTask(msg);


            default:
                return null;
        }
    }

}// end
