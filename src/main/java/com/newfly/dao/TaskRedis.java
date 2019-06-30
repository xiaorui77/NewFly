package com.newfly.dao;

import com.newfly.pojo.Task;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Repository
public class TaskRedis
{
    private final JedisPool jedisPool;

    public TaskRedis(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    // 保存任务列表
    public void saveTaskList(List<Integer> taskCount) {
        Jedis jedis = jedisPool.getResource();

        jedis.rpush("main_task", "task");
        for (Integer sub : taskCount) {
            jedis.rpush("main_task", sub.toString());
        }
        jedis.close();
    }


    // 保存玩家主线任务进度
    public void saveMainTask(String playerId, Task task) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("player:" + playerId, "task", String.valueOf(task.getTaskId()));
        jedis.hset("player:" + playerId, "sub_task", String.valueOf(task.getSubTask()));
        jedis.close();
    }

    // 查询玩家主线任务进度
    public Task getMainTask(String playerId) {
        Jedis jedis = jedisPool.getResource();
        Task task = new Task();
        task.setTaskId(Integer.parseInt(jedis.hget("player:" + playerId, "task")));
        task.setSubTask(Integer.parseInt(jedis.hget("player:" + playerId, "sub_task")));
        jedis.close();
        return task;
    }

    // 更新玩家主线任务进度
    public void changeMainTask(String playerId, String task, String subTask) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("player:" + playerId, "task", task);
        jedis.hset("player:" + playerId, "sub_task", subTask);
        jedis.close();
    }

}// end
