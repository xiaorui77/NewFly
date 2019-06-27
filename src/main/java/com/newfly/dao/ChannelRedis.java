package com.newfly.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;


@Repository
public class ChannelRedis
{
    @Autowired
    private JedisPool jedisPool;


    public Set<String> existChannel() {
        Jedis jedis = jedisPool.getResource();
        return jedis.smembers("chatChannel");
    }

}// end
