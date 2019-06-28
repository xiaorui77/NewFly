package com.newfly.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Repository
public class MapSceneRedis
{
    @Autowired
    JedisPool jedisPool;

    // 添加玩家 到世界和场景sets中
    public void add(String playerId, String sceneId) {
        Jedis jedis = jedisPool.getResource();
        // 添加到世界
        jedis.sadd("world", playerId);
        // 添加到相应scene中
        jedis.sadd("scene" + sceneId, playerId);
        jedis.close();
    }

    // 移除玩家
    public void remove(String playerId, String sceneId) {
        Jedis jedis = jedisPool.getResource();
        jedis.srem("world", playerId);
        jedis.srem("scene" + sceneId, playerId);
        jedis.close();
    }

    // 切换场景
    public void switchScene(String playerId, String oldScene, String newScene) {
        Jedis jedis = jedisPool.getResource();
        jedis.srem("scene" + oldScene, playerId);
        jedis.sadd("scene" + newScene, playerId);
        jedis.close();
    }

    // 获取某个场景的玩家列表
    public Set<String> sceneMember(String sceneId) {
        Jedis jedis = jedisPool.getResource();
        return jedis.smembers("scene" + sceneId);
    }

    // 获取世界玩家列表
    public Set<String> worldMember() {
        Jedis jedis = jedisPool.getResource();
        Set<String> member = jedis.smembers("world");
        jedis.close();
        return member;
    }


}// end
