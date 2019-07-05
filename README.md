# 梦幻修仙(NewFly) 游戏服务器

## 使用的工具或技术等
* java maven spring

* netty4.1.32

* Redis mysql

* 多种设计模式
装饰器模式



## 说明
使用maven管理, java8环境



## 游戏介绍
这是一个多人在线网游




## 临时定义数据(后面会保存在数据库)
* 场景列表
1   新手村  
2   十里坡  
3   十里坡副本  


* 人物职业
1   剑灵


* 公共聊天频道
0 无
1 世界
2 当前scene
3 附近
4 当前家族
5  当前队伍
大于10000 公共聊天频道的个人消息


* 物品分类




## Redis数据库结构说明(不需要自己建立,程序会自动建立)
* 玩家信息 n个 (hash结构) **player:id**

* 角色信息 **sole**

* 世界 (sets结构) **world** 保存所有玩家id
* 场景 n个 (sets结构) key为**scene:Id** 保存此场景中所有玩家的id

* 世界队伍列表 (sets结构) **world_team** 保存所有队伍的id
* 场景队伍列表 n个 (sets结构) **scene_team:id** 保存场景的所有队伍的id
* 每个队伍的详情列表 (hash结构) **team:id** 保存队伍的name captainId memberNum 

* 场景列表 **scene** (sets结构)
* 每个场景的玩家列表 n个 key为scene+sceneId value为成员玩家id

* 主线任务列表 **main_task** (hash结构)
    field task+id

* 战斗列表 n个 (hash结构) **battle_single:id**  保存每场战斗信息
* 怪物信息表 n个 (hash结构) **monster:id**

* 玩家物品表 n个 (hash结构) **item:playerId** 保存玩家物品信息
* 市场物品表 (hash结构) **item:market**



装备位置
-1 上衣
-2 腰带
-3 护腕
-4 护手
-5 裤子
-6 鞋子
-7 翅膀
-8 武器
-9 护符一
-10 护符二
-11 戒指一
-12 戒指二





装备kind
  XX X XX
职业 位置 

物品ID生成 
XXXX

普通物品kind < 1000
装备kind     > 1000

id:kind:backpackIndex:战力:强化等级:是否绑定(0/1):value1:value2:value3
