package com.newfly.mapper;


import com.newfly.pojo.Combat;

import java.util.List;

/*
 * 多个游戏基本信息的加载
 * 1. 任务详情
 * 2. 物品详情
 * */
public interface GameInfoMapper
{
    Integer[] queryTaskCount();

    // 查询怪物信息
    List<Combat> queryMonster();
}
