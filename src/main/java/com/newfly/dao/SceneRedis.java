package com.newfly.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Repository
public class SceneRedis
{
    @Autowired
    JedisPool jedisPool;

    // 添加玩家 到世界和场景sets中
    public void add(String playerId, String sceneId) {
        Jedis jedis = jedisPool.getResource();
        // 添加到世界
        jedis.sadd("world", playerId);
        // 添加到相应scene中
        jedis.sadd("scene:" + sceneId, playerId);
        jedis.close();
    }

    // 移除玩家
    public void remove(String playerId, String sceneId) {
        Jedis jedis = jedisPool.getResource();
        jedis.srem("world", playerId);
        jedis.srem("scene:" + sceneId, playerId);
        jedis.close();
    }

    // 玩家所属场景转变
    public void switchScene(String playerId, String oldScene, String newScene) {
        Jedis jedis = jedisPool.getResource();

        // 场景玩家修改
        jedis.srem("scene:" + oldScene, playerId);
        jedis.sadd("scene:" + newScene, playerId);
        jedis.close();
    }

    // 获取某个场景的玩家列表
    public Set<String> sceneMember(String sceneId) {
        Jedis jedis = jedisPool.getResource();
        Set<String> members = jedis.smembers("scene:" + sceneId);
        jedis.close();
        return members;
    }

    // 获取世界玩家列表
    public Set<String> worldMember() {
        Jedis jedis = jedisPool.getResource();
        Set<String> member = jedis.smembers("world");
        jedis.close();
        return member;
    }

    // 时间和场景移除队伍
    public void removeTeam(String teamId, String sceneId) {
        Jedis jedis = jedisPool.getResource();
        jedis.srem("world_team", teamId);
        jedis.srem("scene_team:" + sceneId, teamId);
        jedis.close();
    }


}// end
