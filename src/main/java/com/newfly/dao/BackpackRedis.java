package com.newfly.dao;


import com.newfly.pojo.Equipment;
import com.newfly.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;


@Repository
public class BackpackRedis
{
    @Autowired
    JedisPool jedisPool;

    // 是否有某玩家的背包信息
    public boolean hasBackpack(String playerId) {
        Jedis jedis = jedisPool.getResource();
        boolean exists = jedis.exists("item:" + playerId);
        jedis.close();
        return exists;
    }

    // 查询背包中的物品/装备
    public List<Item> getBackpackItem(String playerId) {
        Jedis jedis = jedisPool.getResource();
        List<Item> items = new ArrayList<>();

        List<String> members = jedis.hvals("item:" + playerId);
        jedis.close();
        if (members.isEmpty())
            return null;

        for (String m : members) {
            String[] strings = m.split(":");
            if (Integer.parseInt(strings[3]) < 0)
                continue;
            if (Integer.parseInt(strings[2]) < 10000)
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

        List<String> members = jedis.hvals("item:" + playerId);
        jedis.close();
        if (members.isEmpty())
            return null;

        for (String m : members) {
            String[] strings = m.split(":");
            if (Integer.parseInt(strings[3]) > 0)   // 是不是身上
                continue;
            if (Integer.parseInt(strings[2]) < 10000)   // 是不是装备
                items.add(new Item(m));
            else
                items.add(new Equipment(m));
        }
        return items;
    }

    // 查询背包和身上装备信息
    public List<Item> getCarryAll(String playerId) {
        Jedis jedis = jedisPool.getResource();
        List<Item> items = new ArrayList<>();

        List<String> members = jedis.hvals("item:" + playerId);
        jedis.close();
        if (members.isEmpty())
            return null;

        for (String m : members) {
            if (Integer.parseInt(m.split(":")[2]) < 10000)
                items.add(new Item(m));
            else
                items.add(new Equipment(m));
        }
        return items;
    }


    // 查询某件物品信息
    public Item getItem(String playerId, String itemId) {
        Jedis jedis = jedisPool.getResource();
        Item item;
        String itemString = jedis.hget("item:" + playerId, itemId);
        if (itemString == null)
            return null;
        if (Integer.parseInt(itemString.split(":")[2]) < 10000)
            item = new Item(itemString);
        else
            item = new Equipment(itemString);
        jedis.close();
        return item;
    }

    // 查询物品信息
    public String getItemString(String playerId, String itemId) {
        Jedis jedis = jedisPool.getResource();
        String item = jedis.hget("item:" + playerId, itemId);
        jedis.close();
        return item;
    }

    // 保存物品信息
    public void setItem(String playerId, Item item) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("item:" + playerId, String.valueOf(item.getId()), item.toSaveString());
        jedis.close();
    }

    // 移除指定物品
    public void remove(String playerId, String itemId) {
        Jedis jedis = jedisPool.getResource();
        jedis.hdel("item:" + playerId, itemId);
        jedis.close();
    }

    // 获取指定位置物品
    public Item getItemPosition(String playerId, String position) {
        return null;
    }

    // 获取某玩家下一个空位置
    public int getNextBackpack(String playerId) {
        return 78;
    }


    // 获取市场列表


    // 保存到redis
    public void setCarryAll(String playerId, List<Item> items) {
        Jedis jedis = jedisPool.getResource();
        for (Item item : items) {
            jedis.hset("item:" + playerId, String.valueOf(item.getId()), item.toSaveString());
        }
        jedis.close();
    }


}// end
