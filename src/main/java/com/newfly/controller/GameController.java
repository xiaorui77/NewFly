package com.newfly.controller;

import com.newfly.common.Constant;
import com.newfly.pojo.ResultMessage;
import com.newfly.service.*;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Controller;


@Controller
public class GameController
{

    private final PlayerService playerService;

    private final ChatService chatPublic;

    private final MapSceneService mapService;

    private final TeamService teamService;

    private final TaskService taskService;

    private final BackpackService backpackService;

    public GameController(PlayerService playerService, ChatService chatPublic, MapSceneService mapService, TeamService teamService, TaskService taskService, BackpackService backpackService) {
        this.playerService = playerService;
        this.chatPublic = chatPublic;
        this.mapService = mapService;
        this.teamService = teamService;
        this.taskService = taskService;
        this.backpackService = backpackService;
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

            case Constant.BACKPACK_INFO:    // 背包信息
                return backpackInfo(msg);
            case Constant.BACKPACK_ITEM:    // 背包物品
                return getBackpackItems(msg);

            case Constant.MARKET_LIST:  // 市场列表
                return getMarket(msg);
            case Constant.MARKET_PUT:   // 将物品放到市场
                return putMarket(msg);
            case Constant.MARKET_DOWN:  // 下架商品
                return downMarket(msg);
            case Constant.MARKET_BUG:   // 购买物品
                return bugMarket(msg);

            default:
                return null;
        }
    }


    /*
     * 7.物品,背包,市场 7000
     *
     * */
    // 获取物品信息
    public ResultMessage getItem(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String itemId = strings[1];

        String result = backpackService.getItem(playerId, itemId);
        if (result == null)
            return null;
        return new ResultMessage(Constant.BACKPACK_ITEM_INFO_RETURN, result);
    }

    // 使用物品
    public ResultMessage useItem(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String itemId = strings[1];
        String num = strings[2];

        String result = backpackService.useItem(playerId, itemId, Integer.parseInt(num));
        if (result == null)
            return null;
        return new ResultMessage(Constant.BACKPACK_ITEM_USE_RETURN, result);
    }

    // 穿戴/脱下装备
    public ResultMessage wearEquipment(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String equipmentId = strings[1];
        String position = strings[2];

        String result = backpackService.wearEquipment(playerId, equipmentId, Integer.parseInt(position));
        if (result == null)
            return null;
        return new ResultMessage(Constant.BACKPACK_EQUIPMENT_WEAR_RETURN, result);
    }


    // 获取背包信息
    private ResultMessage backpackInfo(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];

        return new ResultMessage(Constant.BACKPACK_INFO_RETURN, backpackService.backpackInfo(playerId));
    }

    // 获取背包物品
    private ResultMessage getBackpackItems(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String method = strings[1];

        backpackService.getBackpackItems(playerId, method);
        return null;
    }


    // 获取市场列表
    private ResultMessage getMarket(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String method = strings[1];

        backpackService.getMarket(playerId, method);
        return null;
    }

    // 放到市场
    private ResultMessage putMarket(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String itemId = strings[1];
        String price = strings[2];

        backpackService.putMarket(playerId, itemId, Integer.parseInt(price));
        return null;
    }

    // 下架市场
    private ResultMessage downMarket(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String commodityId = strings[1];

        backpackService.downMarket(playerId, commodityId);
        return null;
    }

    // 市场购买物品
    private ResultMessage bugMarket(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String commodityId = strings[1];

        backpackService.bugMarket(playerId, commodityId);
        return null;
    }

}// end
