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

    // 添加玩家
    public void add(String playerId, String sceneId) {

    }

    // 切换场景
    public void switchScene(String playerId, String newScene) {

    }

    // 获取某个场景的玩家列表
    public Set<String> scenePlayers(String sceneId) {
        Jedis jedis = jedisPool.getResource();
        return jedis.smembers("scene" + sceneId);
    }


}// end
