package com.newfly.dao;


import com.newfly.pojo.Equipment;
import com.newfly.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Repository
public class BackpackRedis
{
    @Autowired
    JedisPool jedisPool;

    // 是否有某玩家的背包信息
    public boolean isBackpack(String playerId) {
        Jedis jedis = jedisPool.getResource();
        boolean exists = jedis.exists("item:" + playerId);
        jedis.close();
        return exists;
    }

    // 查询背包中的item/装备
    public List<Item> getBackpackItem(String playerId) {
        Jedis jedis = jedisPool.getResource();
        List<Item> items = new ArrayList<>();

        Set<String> members = jedis.smembers("item:" + playerId);
        jedis.close();
        if (members.isEmpty())
            return null;

        for (String m : members) {
            String[] strings = m.split(":");
            if (Integer.parseInt(strings[2]) < 0)
                continue;
            if (Integer.parseInt(strings[1]) < 10000)
                items.add(new Item(m));
            else
                items.add(new Equipment(m));
        }
        return items;
    }

    // 查询身上的装备
    public List<Item> getWearing(String playerId) {
        Jedis jedis = jedisPool.getResource();
        List<Item> items = new ArrayList<>();

        Set<String> members = jedis.smembers("item:" + playerId);
        jedis.close();
        if (members.isEmpty())
            return null;

        for (String m : members) {
            String[] strings = m.split(":");
            if (Integer.parseInt(strings[2]) > 0)   // 是不是身上
                continue;
            if (Integer.parseInt(strings[1]) < 10000)   // 是不是装备
                items.add(new Item(m));
            else
                items.add(new Equipment(m));
        }
        return items;
    }

    // 查询所有信息
    public List<Item> getCarryAll(String playerId) {
        Jedis jedis = jedisPool.getResource();
        List<Item> items = new ArrayList<>();

        Set<String> members = jedis.smembers("item:" + playerId);
        jedis.close();
        if (members.isEmpty())
            return null;

        for (String m : members) {
            if (Integer.parseInt(m.split(":")[1]) < 10000)
                items.add(new Item(m));
            else
                items.add(new Equipment(m));
        }
        return items;
    }

    // 保存所有到redis
    public void setCarryAll(String playerId, List<Item> items) {
        Jedis jedis = jedisPool.getResource();
        for (Item item : items) {
            jedis.sadd("item:" + playerId, item.toResultContent());
        }
        jedis.close();
    }

}// end
