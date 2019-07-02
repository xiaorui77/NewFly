package com.newfly.dao;

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
    public void setSingle(String playerId, String monsterId) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("battle_single:" + playerId, "player", playerId);
        jedis.hset("battle_single:" + playerId, "monster", monsterId);
        jedis.hset("battle_single:" + playerId, "status", "ready");
        jedis.expire("battle_single:" + playerId, 20 * 60);
        jedis.close();
    }

    // 获取某玩家single战斗信息
    public String getSingle(String playerId, String field) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.hget("battle_single:" + playerId, field);
        jedis.close();
        return value;
    }


}// end
