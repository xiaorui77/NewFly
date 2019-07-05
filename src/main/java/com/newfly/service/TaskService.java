package com.newfly.service;

import com.newfly.common.Constant;
import com.newfly.dao.PlayerRedis;
import com.newfly.mapper.GameInfoMapper;
import com.newfly.mapper.PlayerMapper;
import com.newfly.pojo.ResultMessage;
import com.newfly.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/*
 * 任务系统业务
 *
 * 暂时将角色任务直接保存在msql
 * */
@Service
public class TaskService
{
    @Autowired
    PlayerMapper playerMapper;

    @Autowired
    GameInfoMapper gameInfoMapper;

    @Autowired
    PlayerRedis playerRedis;


    /*
     * 初始化后执行
     * 用来加载任务详情到redis
     * */
    @PostConstruct
    public void initTask() {
        // 查询任务列表
        Integer[] subNums = gameInfoMapper.queryTaskCount();


        // 保存任务到静态数组中
        Task.initSubNum(subNums);

        // 保存任务列表到Redis
        //taskRedis.saveTaskList(taskCount);
    }

    // 任务阶段变化
    public ResultMessage change(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String taskId = strings[1];
        String subTaskId = strings[2];

        // 获取玩家已经完成的任务
        Task task = playerRedis.getMainTask(playerId);
        if (task.changeTask(taskId, subTaskId)) {
            // 保存到数据库并返回
            playerRedis.setMainTask(playerId, taskId, subTaskId);
            return new ResultMessage(Constant.MESSAGE_TASK_CHANGE_RETURN, msg.getBody());
        }
        return null;
    }

    // 查询任务
    public ResultMessage checkTask(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String method = strings[1];

        if (method.equals("main")) {
            Task task = playerRedis.getMainTask(playerId);
            String taskString = task.getTask() + ":" + task.getSubTask();
            return new ResultMessage(Constant.MESSAGE_TASK_QUERY_RETURN, taskString);
        } else if (method.equals("all")) {
            return null;
        } else {
            return null;
        }
    }

    // 接受任务 未实现
    public ResultMessage accept(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String taskId = strings[1];
        String subTaskId = strings[2];

        return null;
    }

    // 完成任务 未实现
    public ResultMessage complete(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String taskId = strings[1];
        String subTaskId = strings[2];

        Task task = new Task();
        if (task.Completable(taskId, subTaskId))
            return new ResultMessage(Constant.MESSAGE_TASK_CHANGE_RETURN, msg.getBody());

        // 修改完成状态

        return null;
    }


}
