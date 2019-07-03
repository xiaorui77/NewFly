package com.newfly.dao;

import com.newfly.pojo.Combat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Repository
public class BattleRedis
{
    @Autowired
    JedisPool jedisPool;

    // 开始战斗
    public String setSingle(Combat player, Combat monster) {
        Jedis jedis = jedisPool.getResource();
        String strId = String.valueOf(player.getId());
        jedis.hset("battle_single:" + strId, "id", String.valueOf(player.getId()));
        jedis.hset("battle_single:" + strId, "player", String.valueOf(player.getId()));
        jedis.hset("battle_single:" + strId, "status", "ready");
        jedis.hset("battle_single:" + strId, "round", "0");
        jedis.expire("battle_single:" + strId, 20 * 60);    // 20分钟后自动结束
        jedis.close();
        return String.valueOf(player.getId());
    }

    // 战斗结束, 删除
    public void removeSingle(String battleId) {
        Jedis jedis = jedisPool.getResource();
        jedis.del("battle_single:" + battleId);
        jedis.close();
    }

    // 获取某玩家single战斗信息
    public String getSingle(String playerId, String field) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.hget("battle_single:" + playerId, field);
        jedis.close();
        return value;
    }

    // 设置回合
    public void incrRound(String playerId) {
        Jedis jedis = jedisPool.getResource();
        jedis.hincrBy("battle_single:" + playerId, "round", 1);
        jedis.close();
    }

    // 设置血量
    public void setHp(String battleId, String unitId, String hp) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("battle_single:" + battleId, "unit" + unitId + "hp", hp);
        jedis.close();
    }

    // 改变血量
    public void changeHp(String battleId, String unitId, int change) {
        Jedis jedis = jedisPool.getResource();
        jedis.hincrBy("battle_single:" + battleId, "unit" + unitId + "hp", change);
        jedis.close();
    }

    // 获取战斗状态
    public String getStatus(String id) {
        Jedis jedis = jedisPool.getResource();
        String status = jedis.hget("battle_single:" + id, "status");
        jedis.close();
        return status;
    }

    // 设置战斗状态
    public void setStatus(String id, String status) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("battle_single:" + id, "status", status);
        jedis.close();
    }


    // 设置全局怪物信息
    public void setMonster(Combat combat) {
        Jedis jedis = jedisPool.getResource();
        String strId = String.valueOf(combat.getId());
        jedis.hset("monster:" + strId, "id", String.valueOf(combat.getId()));
        jedis.hset("monster:" + strId, "hp", String.valueOf(combat.getHp()));
        jedis.hset("monster:" + strId, "attack", String.valueOf(combat.getAttack()));
        jedis.hset("monster:" + strId, "defense", String.valueOf(combat.getDefense()));
        jedis.close();
    }

    // 获取全局怪物信息
    public Combat getMonster(String id) {
        Jedis jedis = jedisPool.getResource();
        Combat combat = new Combat();
        combat.setId(Integer.parseInt(jedis.hget("monster:" + id, "id")));
        combat.setHp(Integer.parseInt(jedis.hget("monster:" + id, "hp")));
        combat.setAttack(Integer.parseInt(jedis.hget("monster:" + id, "attack")));
        combat.setDefense(Integer.parseInt(jedis.hget("monster:" + id, "defense")));
        jedis.close();
        return combat;
    }


    // 获取战斗unit信息
    public Combat getUnit(String battleId, String id) {
        Jedis jedis = jedisPool.getResource();
        Combat combat = new Combat();
        combat.setId(Integer.parseInt(jedis.hget("battle_single:" + battleId, "unit" + id)));
        combat.setHp(Integer.parseInt(jedis.hget("battle_single:" + battleId, "unit" + id + "hp")));
        combat.setAttack(Integer.parseInt(jedis.hget("battle_single:" + battleId, "unit" + id + "attack")));
        combat.setDefense(Integer.parseInt(jedis.hget("battle_single:" + battleId, "unit" + id + "defense")));
        jedis.close();
        return combat;
    }

    // 设置战斗单位信息
    public void setUnit(String battleId, String unitId, Combat combat) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("battle_single:" + battleId, "unit" + unitId, String.valueOf(combat.getId()));
        jedis.hset("battle_single:" + battleId, "unit" + unitId + "hp", String.valueOf(combat.getHp()));
        jedis.hset("battle_single:" + battleId, "unit" + unitId + "attack", String.valueOf(combat.getAttack()));
        jedis.hset("battle_single:" + battleId, "unit" + unitId + "defense", String.valueOf(combat.getDefense()));
        jedis.close();
    }

    // 角色死亡
    public void setDeath(String battleId, String unitId) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("battle_single:" + battleId, "unit" + unitId + "hp", "0");
        jedis.close();
    }

    // 获取某方总血量
    public int getTotalHp(String battleId, String unit) {
        Jedis jedis = jedisPool.getResource();
        int unitId = 1;
        int total = 0;
        if (unit.equals("player"))
            unitId = 4;

        for (int i = 0; i < 3; i++) {
            String hp = jedis.hget("battle_single:" + battleId, "unit" + (unitId + i) + "hp");
            if (hp == null)
                continue;
            total += Integer.parseInt(hp);
        }

        jedis.close();
        return total;
    }

}// end
