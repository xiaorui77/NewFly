package com.newfly.common;


import com.newfly.pojo.Combat;

import java.math.BigDecimal;

/*
 * 计算英雄技能伤害
 * */
public class HeroAbility
{
    // 攻击计算
    public static int attack(Combat origin, Combat target, String ability) {
        // 技能生效
        int result = 1;
        double atk1 = origin.getAttack();
        double atk2 = target.getAttack();
        double def2 = target.getDefense();

        double h = (atk1 * atk1) / (atk2 / 2 + def2 * 1.5);
        BigDecimal a = new BigDecimal(h).setScale(0, BigDecimal.ROUND_HALF_UP);
        target.setHp(target.getHp() - Integer.parseInt(a.toString()));

        if (target.getHp() <= 0) {
            target.setHp(0);
            result |= 0x4;
        }
        return result;
    }

}// end
