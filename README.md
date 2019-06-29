# 梦幻修仙(NewFly) 游戏服务器

## 使用的工具或技术等
* java maven spring

* netty4.1.32

* Redis mysql


## 说明
使用maven管理, java8环境



## 游戏介绍
这是一个多人在线网游




## 数据包格式(content目前使用字符串形式)
length(2B) type(2B) content

## type分类
* 其他类 2000
* 玩家类 3000
* 战斗类 5000


### 登录 3100
* 玩家登录 3111  
name:password
* 返回 3112  
id:name:profession:grade:exp:money:scene:x:y

* 玩家退出 3113  
* 返回 无  

* 注册 3121  
* 注销 3223  


### 队伍,家族,聊天等 3200

* 好友聊天 3211  
playerId:targetID:message
* 返回 3212  
fromId:message

* 公共聊天 3221  
playerId:channelId:message
* 返回 3222  
channelId:fromId:fromName:message


* 创建队伍 3231  
playerId
* 创建成功返回 3232  
teamId
* 查询队伍 3233  
playerId:world    世界所有的队伍  
playerId:scene  当前场景中的队伍  
playerId:near   附近队伍,暂时不做  
playerId:teamId 查询指定队伍的信息  
* 成功返回 3234  
队伍id:队伍名:队长id:队长等级:队长职业:队伍人数  
* 加入队伍请求 3235  
playerId:teamId
* 加入成功/更新队伍返回 3236  
teamId:captainId:member1:member2  
* 退出队伍请求 3237  
playerId  
* 成功返回 3238  
teamId  



### 副本,场景,玩家移动等 3300
* 玩家移动 3311
id:sceneId:x:y
* 返回 3312
id:sceneId:x:y



### 背包 3400
* 获取背包 3411
未实现
* 返回背包 3412
未实现
* 背包变化 3414
未实现

* 放到市场 3421
playerId:goodId:num
* 获取列表 3423
* 购买 3424
* 不卖了 3427
* 下架商品 3429


### 任务 3500
* 接主线任务 351
playerId:taskId:subTaskId
* 返回
playerId:taskId:subTaskId




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

* 世界 (sets结构) **world** 保存所有玩家id
* 场景 n个 (sets结构) key为**scene:Id** 保存此场景中所有玩家的id

* 世界队伍列表 (sets结构) **world_team** 保存所有队伍的id
* 场景队伍列表 n个 (sets结构) **scene_team:id** 保存场景的所有队伍的id
* 每个队伍的详情列表 (hash结构) **team:id** 保存队伍的name captainId memberNum 

* 场景列表 **scene** (sets结构)
* 每个场景的玩家列表 n个 key为scene+sceneId value为成员玩家id
