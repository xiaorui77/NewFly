package com.newfly.controller;


import com.newfly.common.Constant;
import com.newfly.common.RequestType;
import com.newfly.pojo.ResultMessage;
import com.newfly.service.BattleService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Controller
public class BattleController
{
    @Autowired
    BattleService battleService;


    // 请求转发
    ResultMessage handle(ChannelHandlerContext ctx, ResultMessage msg) {
        int type = msg.getType();
        Method[] methods = BattleController.class.getDeclaredMethods();
        for (Method method : methods) {
            RequestType requestType = method.getAnnotation(RequestType.class);
            if (requestType == null)
                continue;

            if (type == requestType.value()) {
                try {
                    return (ResultMessage) method.invoke(this, msg);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        // 该type没有找到
        return null;
    }


    // 进入单人战斗
    @RequestType(Constant.BATTLE_SINGLE)
    public ResultMessage enterSingle(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String monsterId = strings[1];

        String body = battleService.enterSingle(playerId, monsterId);
        return new ResultMessage(Constant.BATTLE_SINGLE_RETURN, body);
    }

    // 选择技能
    @RequestType(Constant.BATTLE_ABILITY_SELECT)
    public ResultMessage selectAbility(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String battleId = strings[0];
        String originId = strings[1];
        String abilityId = strings[2];
        String targetId = strings[3];

        // 发动技能
        if (battleService.selectAbility(battleId, originId, abilityId, targetId))
            battleService.selectAbility(battleId, targetId, abilityId, originId);
        return null;
    }

    // 其他

}// end
