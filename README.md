# 梦幻修仙(NewFly) 游戏服务器

## 使用的工具或技术等
* java maven spring

* netty4.1.32

* Redis mysql



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

