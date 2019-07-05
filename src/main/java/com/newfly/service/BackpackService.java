package com.newfly.service;

import com.newfly.common.Constant;
import com.newfly.common.SocketChannelMap;
import com.newfly.dao.BackpackRedis;
import com.newfly.dao.PlayerRedis;
import com.newfly.dao.SceneRedis;
import com.newfly.mapper.BackpackMapper;
import com.newfly.mapper.GameInfoMapper;
import com.newfly.mapper.PlayerMapper;
import com.newfly.pojo.Equipment;
import com.newfly.pojo.Item;
import com.newfly.pojo.Player;
import com.newfly.pojo.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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

    @Autowired
    SceneRedis sceneRedis;

    @Autowired
    PlayerMapper playerMapper;

    @Autowired
    PlayerRedis playerRedis;

    @Autowired
    GameInfoMapper gameInfoMapper;


    /*
     * 初始化后执行
     * 用来加载怪物详情到redis monster(hash)
     * */
    @PostConstruct
    public void initMonster() {
        List<Item> items = backpackMapper.queryItems(9999);
        items.addAll(backpackMapper.queryEquipment(9999));

        // 保存到redis
        backpackRedis.setCarryAll("market", items);
    }


    static Item generateItem(int kind, int num) {
        return null;
    }


    // 获取单件物品信息
    public String getItem(String playerId, String itemId) {
        if (!backpackRedis.hasBackpack(playerId))
            return null;

        return backpackRedis.getItemString(playerId, itemId);
    }

    // 使用物品
    public String useItem(String playerId, String itemId, int num) {
        if (!backpackRedis.hasBackpack(playerId))
            return null;

        Item item = backpackRedis.getItem(playerId, itemId);
        if (item.getNum() < num)
            return null;
        item.setNum(item.getNum() - num);
        backpackRedis.setItem(playerId, item);
        return "success";
    }

    // 穿戴/脱下装备
    public String wearEquipment(String playerId, String equipmentId, int position) {
        if (!backpackRedis.hasBackpack(playerId))
            return null;

        Equipment equipment = (Equipment) backpackRedis.getItem(playerId, equipmentId);
        if (equipment == null || position * equipment.getBackpackIndex() > 0)
            return null;
        int t = equipment.getBackpackIndex();
        if (equipment.getBackpackIndex() < 0) {
            // 脱下
            if (position > 0) {
                Item item = backpackRedis.getItemPosition(playerId, String.valueOf(position));
                // 如果没有物品
                if (item == null) {
                    equipment.setBackpackIndex(position);
                    backpackRedis.setItem(playerId, equipment);
                    return t + ":" + position;
                }
            }
            // 自动判断, 获取空格子
            int next = backpackRedis.getNextBackpack(playerId);
            if (next == 0)
                return null;
            equipment.setBackpackIndex(next);
            backpackRedis.setItem(playerId, equipment);
            return t + ":" + next;
        } else if (equipment.getBackpackIndex() > 0) {
            // 穿上
            if (position < 0) {
                Item item = backpackRedis.getItemPosition(playerId, String.valueOf(position));
                // 如果目标位置有装备, 则交换位置
                if (item != null) {
                    item.setBackpackIndex(equipment.getBackpackIndex());
                    backpackRedis.setItem(playerId, item);
                }
                equipment.setBackpackIndex(position);
                backpackRedis.setItem(playerId, equipment);
                return t + ":" + position;
            }
            // 自动判断
            // 获取装备位置
            int newPosition = 3;
            equipment.setBackpackIndex(newPosition);
            backpackRedis.setItem(playerId, equipment);
            return t + ":" + newPosition;
        }
        return null;
    }


    // 获取背包信息
    public String backpackInfo(String playerId) {
        return "100";
    }

    // 获取背包物品
    public ResultMessage getBackpackItems(String playerId, String method) {
        List<Item> items;

        // 判断方式
        switch (method) {
            case "backpack":
                if (backpackRedis.hasBackpack(playerId))
                    items = backpackRedis.getBackpackItem(playerId);
                else {
                    items = backpackMapper.queryItems(Integer.parseInt(playerId));
                    items.addAll(backpackMapper.queryEquipment(Integer.parseInt(playerId)));
                }
                break;
            case "wearing":
                if (backpackRedis.hasBackpack(playerId))
                    items = backpackRedis.getWearing(playerId);
                else
                    items = backpackMapper.queryWearing(Integer.parseInt(playerId));
                break;
            case "all":
                if (backpackRedis.hasBackpack(playerId)) {
                    items = backpackRedis.getCarryAll(playerId);
                } else {
                    items = backpackMapper.queryItems(Integer.parseInt(playerId));
                    items.addAll(backpackMapper.queryWearing(Integer.parseInt(playerId)));
                    items.addAll(backpackMapper.queryEquipment(Integer.parseInt(playerId)));
                }
                break;
            default:
                items = new ArrayList<>();
        }

        // 发送消息
        for (Item item : items)
            SocketChannelMap.sendTo(playerId, Constant.BACKPACK_ITEM_RETURN, item.toResultContent());

        return null;
    }


    // 市场列表 playerId为9999
    public void getMarket(String playerId, String method) {
        List<Item> items;

        // 判断方式
        if (method.equals("all")) {
            if (backpackRedis.hasBackpack("market"))
                items = backpackRedis.getCarryAll("market");
            else {
                items = backpackMapper.queryItems(9999);
                items.addAll(backpackMapper.queryEquipment(9999));
                // 顺便保存到redis
                backpackRedis.setCarryAll("market", items);
            }
        } else {
            items = new ArrayList<>();
        }

        // 发送消息
        for (Item item : items)
            SocketChannelMap.sendTo(playerId, Constant.BACKPACK_ITEM_RETURN, item.toCommodityContent());
    }

    // 放到市场
    public void putMarket(String playerId, String itemId, int price) {
        Item item = backpackRedis.getItem(playerId, itemId);
        if (item == null)
            return;
        // 必须事在身上的
        if (item.getBackpackIndex() < 0)
            return;

        // 移除该玩家物品信息
        backpackRedis.remove(playerId, itemId);

        // 给玩家发送背包变化
        int kind = item.getKind();
        item.setKind(0);
        SocketChannelMap.sendTo(playerId, Constant.BACKPACK_ITEM_CHANGE_RETURN, item.toResultContent());

        // 保存到市场列表 并广播给其他玩家市场变化
        item.setBackpackIndex(price);   // 用index来保存价格
        item.setKind(kind);
        item.setOwner(Integer.parseInt(playerId));
        backpackRedis.setItem("market", item);

        Set<String> member = sceneRedis.worldMember();
        SocketChannelMap.sendAll(member, Constant.MARKET_CHANGE_RETURN, item.toCommodityContent());
    }

    // 下架物品
    public void downMarket(String playerId, String commodityId) {
        Item commodity = backpackRedis.getItem("market", commodityId);
        if (commodity == null)
            return;

        if (!playerId.equals(String.valueOf(commodity.getOwner())))
            return;

        int next = backpackRedis.getNextBackpack(playerId);
        if (next == 0)
            return; // 没有空格子

        commodity.setBackpackIndex(next);
        backpackRedis.setItem(playerId, commodity); // 给玩家添加
        backpackRedis.remove("market", commodityId);    // 从市场列表移除

        // 背包变化
        SocketChannelMap.sendTo(playerId, Constant.BACKPACK_ITEM_CHANGE_RETURN, commodity.toResultContent());

        // 广播市场变化
        Set<String> member = sceneRedis.worldMember();
        SocketChannelMap.sendAll(member, Constant.MARKET_REDUCE, String.valueOf(commodity.getId()));
    }

    // 市场购买物品
    public void bugMarket(String playerId, String commodityId) {
        Item commodity = backpackRedis.getItem("market", commodityId);
        if (commodity == null)
            return;

        // 判断不是自己的
        if (playerId.equals(String.valueOf(commodity.getOwner())))
            return;

        // 有没有空位置
        int next = backpackRedis.getNextBackpack(playerId);
        if (next == 0)
            return;

        // 判断金钱
        int price = commodity.getBackpackIndex();
        Player player = playerRedis.getPlayer(playerId);
        if (player.getMoney() < price)
            return;

        // 金钱变化
        Player seller = playerRedis.getPlayer(String.valueOf(commodity.getOwner()));
        if (seller == null) {
            // 卖家不在线,直接更新数据库
            seller = new Player();
            seller.setId(commodity.getOwner());
            seller.setMoney(price);
            playerMapper.increaseMoney(seller);
        } else {
            seller.changeMoney(price);
            playerRedis.setMoney(seller.getStrId(), seller.getMoney());
        }
        // 更新玩家金币
        player.changeMoney(0 - commodity.getBackpackIndex());
        playerRedis.setMoney(playerId, player.getMoney());

        // 市场和玩家物品变化保存
        commodity.setBackpackIndex(next);
        commodity.setOwner(Integer.parseInt(playerId));
        backpackRedis.remove("market", commodityId);
        backpackRedis.setItem(playerId, commodity);

        // 购买成功
        SocketChannelMap.sendTo(playerId, Constant.MARKET_BUG_RETURN, commodityId);

        // 金钱变化播报
        SocketChannelMap.sendTo(playerId, Constant.PLAYER_GRADE_MONEY_RETURN, player.toExpMoney());
        SocketChannelMap.sendTo(seller.getStrId(), Constant.PLAYER_GRADE_MONEY_RETURN, seller.toExpMoney());

        // 买家背包变化播报
        SocketChannelMap.sendTo(playerId, Constant.BACKPACK_ITEM_CHANGE_RETURN, commodity.toResultContent());

        // 市场变化广播
        Set<String> member = sceneRedis.worldMember();
        SocketChannelMap.sendAll(member, Constant.MARKET_REDUCE, commodityId);

    }


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
