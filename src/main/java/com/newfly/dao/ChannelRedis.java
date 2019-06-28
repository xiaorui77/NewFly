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


    // 查询某个频道的成员
    public Set<String> channelMember(int channelId) {
        Jedis jedis = jedisPool.getResource();
        return jedis.smembers("chatChannel");
    }

    // 是否存在某个频道
    public boolean existChannel(int channel) {
        Jedis jedis = jedisPool.getResource();
        return jedis.sismember("chatChannel", String.valueOf(channel));
    }

}// end
