package com.newfly.dao;


import com.newfly.common.RedisUtil;
import com.newfly.pojo.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPool;


@Repository
public class PlayerRedis
{
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private JedisPool jedisPool;


    // 设置player
    public void hsetPlayer(Player player) {
        String strID = String.valueOf(player.getId());
        redisUtil.hset(strID, "name", player.getName());
        redisUtil.hset(strID, "grade", String.valueOf(player.getGrade()));
        redisUtil.hset(strID, "exp", player.getName());
        redisUtil.hset(strID, "money", player.getName());
        redisUtil.hset(strID, "x", String.valueOf(player.getX()));
        redisUtil.hset(strID, "y", String.valueOf(player.getY()));
    }

    public void hdelPlayer(int id) {
        redisUtil.hdel(String.valueOf(id));
    }
}
