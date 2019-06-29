package com.newfly.dao;


import com.newfly.pojo.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;


@Repository
public class PlayerRedis
{
    @Autowired
    private JedisPool jedisPool;


    // 设置玩家信息, key为玩家id
    public void savePlayer(Player player) {
        Jedis jedis = jedisPool.getResource();
        String strID = "player:" + player.getId();
        jedis.hset(strID, "id", String.valueOf(player.getId()));
        jedis.hset(strID, "name", player.getName());
        jedis.hset(strID, "profession", String.valueOf(player.getProfession()));
        jedis.hset(strID, "grade", String.valueOf(player.getGrade()));
        jedis.hset(strID, "scene", String.valueOf(player.getScene()));
        jedis.hset(strID, "exp", String.valueOf(player.getExp()));
        jedis.hset(strID, "money", String.valueOf(player.getMoney()));
        jedis.hset(strID, "x", String.valueOf(player.getX()));
        jedis.hset(strID, "y", String.valueOf(player.getY()));
        jedis.close();
    }

    // 移除玩家信息
    public void removePlayer(String playerId) {
        Jedis jedis = jedisPool.getResource();
        jedis.del("player:" + playerId);
        jedis.close();
    }

    // 获取玩家信息 Set集合形式
    public Map<String, String> getInfo(String playerId) {
        Jedis jedis = jedisPool.getResource();
        Map<String, String> playerInfo = jedis.hgetAll("player:" + playerId);
        jedis.close();
        if (playerInfo.isEmpty())
            return null;
        return playerInfo;
    }

    // 获取玩家信息 字符串形式
    public String playerInfo(String playerId) {
        Jedis jedis = jedisPool.getResource();
        Map<String, String> player = jedis.hgetAll("player:" + playerId);
        if (player.isEmpty())
            return null;
        String string = player.get("id") + ":" + player.get("name") + ":" + player.get("profession") + ":" + player.get("grade") + ":" + player.get("exp") + ":" + player.get("money") + ":" + player.get("scene") + ":" + player.get("x") + ":" + player.get("y");
        jedis.close();
        return string;
    }

    // 是否是队长
    public boolean isCaptain(String playerId) {
        Jedis jedis = jedisPool.getResource();
        // 是否有队伍
        String teamId = jedis.hget("player:" + playerId, "team");
        if (teamId == null)
            return false;

        // 获取该队伍的队长id并判断
        String captainId = jedis.hget("team:" + teamId, "captainId");
        return captainId.equals(playerId);
    }

    // 获取玩家所在的场景
    public String getScene(String playerId) {
        Jedis jedis = jedisPool.getResource();
        String sceneId = jedis.hget("player:" + playerId, "scene");
        jedis.close();
        return sceneId;
    }

    // 玩家切换场景
    public void switchScene(String playerId, String oldScene, String newScene) {
        Jedis jedis = jedisPool.getResource();

        // 玩家所属场景修改
        jedis.hset("player:" + playerId, "scene", newScene);
        jedis.close();
    }

    // 玩家加入队伍, 返回队长id
    public String joinTeam(String playerId, String teamId) {
        Jedis jedis = jedisPool.getResource();
        jedis.hsetnx("player:" + playerId, "team", teamId);
        String captainId = jedis.hget("team:" + teamId, "captainId");
        jedis.close();
        return captainId;
    }

    // 玩家离开队伍
    public void leaveTeam(String playerId) {
        Jedis jedis = jedisPool.getResource();
        jedis.hdel("player:" + playerId, "team");
        jedis.close();
    }

    // 获取玩家所在队伍
    public String getTeam(String playerId) {
        Jedis jedis = jedisPool.getResource();
        String teamId = jedis.hget("player:" + playerId, "team");
        jedis.close();
        return teamId;
    }

    // 某个玩家是否有队伍
    public boolean existTeam(String playerId) {
        Jedis jedis = jedisPool.getResource();
        boolean result = jedis.hexists("player:" + playerId, "team");
        jedis.close();
        return result;
    }

    // 玩家移动
    public boolean movetoPlayer(String playerId, String x, String y) {
        Jedis jedis = jedisPool.getResource();
        if (jedis.hexists("player:" + playerId, "id")) {
            jedis.hset("player:" + playerId, "x", x);
            jedis.hset("player:" + playerId, "y", y);
            jedis.close();
            return true;
        }
        return false;
    }


    // 获取玩家name
    public String getName(String palyerId){
        Jedis jedis = jedisPool.getResource();
        String name = jedis.hget("player:" + palyerId, "name");
        jedis.close();
        return name;
    }

}// end
