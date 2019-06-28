package com.newfly.dao;


import com.newfly.pojo.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Repository
public class PlayerRedis
{
    @Autowired
    private JedisPool jedisPool;


    // 设置玩家信息, key为玩家id
    public void savePlayer(Player player) {
        Jedis jedis = jedisPool.getResource();
        String strID = String.valueOf(player.getId());
        jedis.hset(strID, "id", String.valueOf(player.getId()));
        jedis.hset(strID, "name", player.getName());
        jedis.hset(strID, "grade", String.valueOf(player.getGrade()));
        jedis.hset(strID, "scene", String.valueOf(player.getScene()));
        jedis.hset(strID, "exp", player.getName());
        jedis.hset(strID, "money", player.getName());
        jedis.hset(strID, "x", String.valueOf(player.getX()));
        jedis.hset(strID, "y", String.valueOf(player.getY()));
        jedis.close();
    }

    // 移除玩家信息
    public void removePlayer(String playerId) {
        Jedis jedis = jedisPool.getResource();
        jedis.del(playerId);
        jedis.close();
    }

    // 获取玩家所在的场景
    public String getScene(String playerId) {
        Jedis jedis = jedisPool.getResource();
        String sceneId = jedis.hget(playerId, "scene");
        jedis.close();
        return sceneId;
    }

    // 某个玩家是否有队伍
    public boolean existTeam(String id) {
        Jedis jedis = jedisPool.getResource();
        boolean result = jedis.hexists(id, "team");
        jedis.close();
        return result;
    }

    // 玩家移动
    public boolean movetoPlayer(String playerId, String x, String y) {
        Jedis jedis = jedisPool.getResource();
        if (jedis.hexists(playerId, "id")) {
            jedis.hset(playerId, "x", x);
            jedis.hset(playerId, "y", y);
            jedis.close();
            return true;
        }
        return false;
    }
}
