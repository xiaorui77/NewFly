package com.newfly.mapper;

import com.newfly.pojo.Combat;
import com.newfly.pojo.Player;
import com.newfly.pojo.Task;

public interface PlayerMapper
{
    // 查询玩家信息
    Player queryPlayer(int id);

    // 查询玩家主线任务进度
    Task queryTask(int id);

    // 查询玩家战斗信息
    Combat queryCombat(int id);

    // 保存玩家数据
    void updatePlayer(Player player);

    // 更新主线任务进度
    void updateMainTask(Task task);

    // 更新玩家战斗信息
    void updateCombat(Combat combat);

    // 玩家增加金币
    void increaseMoney(Player player);
}
