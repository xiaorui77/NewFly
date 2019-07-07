package com.newfly.util;

import com.newfly.mapper.BackpackMapper;
import com.newfly.pojo.Item;
import com.newfly.service.ChatService;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@Component
public class SyncMQ implements Runnable
{
    private static JedisPool jedisPool;

    private final BackpackMapper backpackMapper;

    private final ChatService chatService;

    public SyncMQ(JedisPool pool, BackpackMapper backpackMapper, ChatService chatService) {
        jedisPool = pool;
        this.backpackMapper = backpackMapper;
        this.chatService = chatService;
    }

    public static void add(String topic, String id) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(1);

        // 如果有delete则转化为update
        if (jedis.sismember(topic + "_delete", id)) {
            jedis.srem(topic + "_delete", id);
            jedis.lrem(topic, -1, "delete_" + id);

            jedis.rpush(topic, "update_" + id);
            jedis.sadd(topic + "_update", id);
            jedis.close();
            return;
        }

        jedis.rpush(topic, "add_" + id);
        jedis.sadd(topic + "_add", id);
        jedis.close();
    }

    public static void update(String topic, String id) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(1);

        // 如果有的话则不必再次update
        if (jedis.sismember(topic + "_update", id) || jedis.sismember(topic + "_add", id))
            return;

        jedis.rpush(topic, "_update" + id);
        jedis.sadd(topic + "_update", id);
        jedis.close();
    }


    public static void delete(String topic, String id) {
        Jedis jedis = jedisPool.getResource();

        // 如果有add和update的话先删除. 为了节省性能可以不管

        jedis.rpush(topic + "_delete", id);
        jedis.sadd(topic + "_delete", id);
        jedis.close();
    }

    // 广播消息
    public static void addPublicMessage(String playerId, String channelId, String message) {
        Jedis jedis = jedisPool.getResource();
        jedis.rpush("public_chat", channelId + ":" + playerId + ":" + message);
        jedis.close();
    }

    public void itemRun() {
        Jedis jedis = jedisPool.getResource();
        while (true) {
            String itemStr = jedis.lpop("item");
            String method = itemStr.split("_")[0];
            String itemId = itemStr.split("_")[1];

            Map<String, String> itemMap;
            Item item;
            switch (method) {
                case "update":
                    itemMap = jedis.hgetAll("item:" + itemId);
                    if (itemMap.isEmpty())
                        continue;
                    item = null;
                    backpackMapper.updateItem(item);
                    break;

                case "delete":
                    backpackMapper.deleteItem(itemId);
                    break;

                case "add":
                    itemMap = jedis.hgetAll("item:" + itemId);
                    if (itemMap.isEmpty())
                        continue;
                    item = null;
                    backpackMapper.insertItem(item);
                    break;
            }
        }
        //jedis.close();
    }

    private void chatRun() {
        Jedis jedis = jedisPool.getResource();
        while (true) {
            String msg = jedis.lpop("chat_public");
            if (msg == null) {
                try {
                    Thread.sleep(500);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
            }

            String[] strings = msg.split(":");
            chatService.chatPublic(strings[0], strings[1], strings[2]);
        }
    }

    @Override
    public void run() {
        chatRun();
    }
}// end
