package com.newfly.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Repository
public class TeamRedis
{
    @Autowired
    private JedisPool jedisPool;

    // 当前分配的队伍ID 10000-100000
    private int curTeamID = 10000;


    // 创建队伍
    public int createTeam(String playerId) {
        Jedis jedis = jedisPool.getResource();
        jedis.sadd("team", String.valueOf(playerId));
        jedis.zadd("team" + curTeamID, 1, playerId);
        jedis.close();
        return curTeamID++;
    }

    // 获取队伍成员
    public Set<String> teamMember(String teamId) {
        Jedis jedis = jedisPool.getResource();
        Set<String> member = jedis.smembers("team" + teamId);
        jedis.close();
        return member;
    }

}// end
