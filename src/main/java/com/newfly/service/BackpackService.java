package com.newfly.service;

import com.newfly.common.Constant;
import com.newfly.common.SocketChannelMap;
import com.newfly.dao.BackpackRedis;
import com.newfly.mapper.BackpackMapper;
import com.newfly.pojo.Item;
import com.newfly.pojo.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;


/*
 * 背包,市场业务系统
 * */
@Controller
public class BackpackService
{
    @Autowired
    BackpackMapper backpackMapper;

    @Autowired
    BackpackRedis backpackRedis;


    static Item generateItem(int kind, int num) {
        return null;
    }


    // 使用物品
    public ResultMessage useItem(ResultMessage msg) {

        return null;
    }

    // 穿戴/脱下装备
    public ResultMessage wearEquipment(ResultMessage msg) {
        return null;
    }


    // 获取背包信息
    public ResultMessage backpackInfo(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];


        return new ResultMessage(Constant.BACKPACK_INFO_RETURN, "100");
    }

    // 获取背包物品
    public ResultMessage getBackpackItems(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String method = strings[1];

        List<Item> items = new ArrayList<>();

        switch (method) {
            case "backpack":
                if (backpackRedis.isBackpack(playerId))
                    items = backpackRedis.getBackpackItem(playerId);
                else {
                    items = backpackMapper.queryItems(Integer.parseInt(playerId));
                    items.addAll(backpackMapper.queryEquipment(Integer.parseInt(playerId)));
                }
                break;
            case "wearing":
                if (backpackRedis.isBackpack(playerId))
                    items = backpackRedis.getWearing(playerId);
                else
                    items = backpackMapper.queryWearing(Integer.parseInt(playerId));
                break;
            case "all":
                if (backpackRedis.isBackpack(playerId)) {
                    items = backpackRedis.getCarryAll(playerId);
                } else {
                    items = backpackMapper.queryItems(Integer.parseInt(playerId));
                    items.addAll(backpackMapper.queryWearing(Integer.parseInt(playerId)));
                    items.addAll(backpackMapper.queryEquipment(Integer.parseInt(playerId)));
                }
                break;
        }

        // 发送消息
        for (Item item : items)
            SocketChannelMap.sendTo(playerId, Constant.BACKPACK_ITEM_RETURN, item.toResultContent());

        return null;
    }


    // 市场


    // 查询玩家物品到redis
    public void queryToRedis(String playerId) {
        // 从数据库中查询所有
        List<Item> items = backpackMapper.queryItems(Integer.parseInt(playerId));
        items.addAll(backpackMapper.queryWearing(Integer.parseInt(playerId)));
        items.addAll(backpackMapper.queryEquipment(Integer.parseInt(playerId)));

        // 保存到redis
        backpackRedis.setCarryAll(playerId, items);
    }


}// end
