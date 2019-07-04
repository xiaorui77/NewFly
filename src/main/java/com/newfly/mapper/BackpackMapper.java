package com.newfly.mapper;

import com.newfly.pojo.Item;

import java.util.List;

public interface BackpackMapper
{
    // 根据用户Id查询物品
    List<Item> queryItems(int playerId);

    // 根据用户Id查询未穿戴装备
    List<Item> queryEquipment(int playerId);

    // 查询用户已经穿戴装备
    List<Item> queryWearing(int playerId);

}
