package com.newfly.service;

import com.newfly.pojo.ResultMessage;
import org.springframework.stereotype.Controller;


/*
 * 背包业务
 * */
@Controller
public class KnapsackService
{


    // 获取背包列表
    public ResultMessage Knapsack(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];


        return null;
    }

}// end
