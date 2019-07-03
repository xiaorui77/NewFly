package com.newfly.service;


import com.newfly.common.Constant;
import com.newfly.common.HeroAbility;
import com.newfly.common.SocketChannelMap;
import com.newfly.dao.BattleRedis;
import com.newfly.dao.PlayerRedis;
import com.newfly.mapper.GameInfoMapper;
import com.newfly.pojo.Combat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class BattleService
{
    @Autowired
    BattleRedis battleRedis;

    @Autowired
    PlayerRedis playerRedis;

    @Autowired
    GameInfoMapper gameInfoMapper;


    /*
     * 初始化后执行
     * 用来加载怪物详情到redis monster(hash)
     * */
    @PostConstruct
    public void initMonster() {
        List<Combat> monsters = gameInfoMapper.queryMonster();
        for (Combat combat : monsters) {
            battleRedis.setMonster(combat);
        }
    }


    // 进入单人战斗
    public String enterSingle(String playerId, String monsterId) {
        String status = battleRedis.getSingle(playerId, "status");
        if (status != null)
            return "已经开始战斗!";
        // 初始化战斗信息
        Combat player = playerRedis.getCombat(playerId);
        Combat monster = battleRedis.getMonster(monsterId);
        String battleId = battleRedis.setSingle(player, monster);
        battleRedis.setUnit(battleId, String.valueOf(5), player);
        battleRedis.setUnit(battleId, String.valueOf(2), monster);
        return battleId + ":success";
    }

    // 选择技能
    public boolean selectAbility(String battleId, String originId, String abilityId, String targetId) {
        String status = battleRedis.getSingle(battleId, "status");
        if (status == null)
            return false;

        // 玩家进行攻击
        Combat origin = battleRedis.getUnit(battleId, originId);
        Combat target = battleRedis.getUnit(battleId, targetId);

        // 攻击计算
        int r = HeroAbility.attack(origin, target, abilityId);
        battleRedis.incrRound(battleId);


        // 如果生效
        if ((r & 0x1) != 0) {
            SocketChannelMap.sendTo(battleId, Constant.BATTLE_ABILITY_SELECT_RETURN, originId + ":" + abilityId + ":" + targetId);

            // 被攻击方状态变化
            battleRedis.setUnit(battleId, targetId, target);
            statusBroadcast(battleId, targetId + ":status:" + target.getHp() + ":" + target.getSp());
        } else {
            return true;
        }

        // 攻击方状态变化
        if ((r & 0x2) != 0) {
            battleRedis.setUnit(battleId, originId, origin);
            statusBroadcast(battleId, originId + ":status:" + origin.getHp() + ":" + origin.getSp());
        }

        // 有角色死亡
        if ((r & 0x4) != 0) {
            statusBroadcast(battleId, targetId + ":death");

            // 战斗是否结束
            if (battleRedis.getTotalHp(battleId, "player") <= 0) {
                // 广播信息
                finishBroadcast(battleId, "defeat:0:0");

                // 移除redis
                battleRedis.setStatus(battleId, "defeat");
                battleRedis.removeSingle(battleId);
                // 保存战斗结果
                return false;
            } else if (battleRedis.getTotalHp(battleId, "monster") <= 0) {
                // 广播信息
                finishBroadcast(battleId, "victory:100:100");

                // 移除redis
                battleRedis.setStatus(battleId, "victory");
                battleRedis.removeSingle(battleId);

                // 保存战斗结果
                return false;
            }
        }

        return true;
    }


    // 是否结束战斗
    private boolean finish() {
        return false;
    }

    // 状态信息广播
    private void statusBroadcast(String teamId, String content) {
        SocketChannelMap.sendTo(teamId, Constant.BATTLE_STATUS_CHANGE_RETURN, content);
    }

    // 战斗结束广播
    private void finishBroadcast(String teamId, String content) {
        SocketChannelMap.sendTo(teamId, Constant.BATTLE_RESULT_RETURN, content);
    }

}// end
