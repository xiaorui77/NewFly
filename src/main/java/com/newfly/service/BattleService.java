package com.newfly.service;


import com.newfly.dao.BattleRedis;
import com.newfly.pojo.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BattleService
{
    @Autowired
    BattleRedis battleRedis;

    // 进入单人战斗
    public String enterSingle(String playerId, String monsterId) {
        String status = battleRedis.getSingle(playerId, "status");
        if (status != null)
            return "已经开始战斗!";
        battleRedis.setSingle(playerId, monsterId);
        return "success";
    }

    // 选择技能
    public ResultMessage selectAbility(String originatorId, String abilityId, String targetId) {
        String status = battleRedis.getSingle(playerId, "status");
        if (status == null)
            return null;
    }

}// end
