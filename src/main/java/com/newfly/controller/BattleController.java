package com.newfly.controller;


import com.newfly.common.Constant;
import com.newfly.pojo.ResultMessage;
import com.newfly.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BattleController
{
    @Autowired
    BattleService battleService;

    // 进入单人战斗装填
    public ResultMessage enterSingle(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String monsterId = strings[1];

        String body = battleService.enterSingle(playerId, monsterId);
        return new ResultMessage(Constant.BATTLE_SINGLE_RETURN, body);
    }

    // 选择技能
    public ResultMessage selectAbility(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String originatorId = strings[0];
        String abilityId = strings[1];
        String targetId = strings[2];
        return battleService.selectAbility(originatorId, abilityId, targetId);
    }

    //

}// end
