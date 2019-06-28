# 梦幻修仙(NewFly) 游戏服务器

## 技术栈 
* java maven spring

* netty4.1.32

* Redis mysql


## 说明
使用maven管理, java8环境
coder文件夹是我自己做的一个备份, 没有任何影响, 可以忽略


## 数据格式(目前使用字符串形式)
length type content

* 其他类 2000
* 玩家类 3000
* 战斗类 5000


### 登录 3100
* 玩家登录 3111
name:password

* 返回 3112
玩家信息(待完成)

* 玩家退出 3113
* 返回 无

* 注册 3121
* 注销 3223


### 队伍,家族,聊天等 3200

* 私聊 3211
id:targetID:message
* 返回 3212
fromId:message

* 公共聊天 3221
id:channelId:message
* 返回 3222
channelId:fromId:message


* 创建队伍 3231
playId
* 创建成功返回
teamId
* 查询队伍
 


### 副本,场景,玩家移动等 3300
* 玩家移动 3311
id:mapID:x:y
* 返回 3312
id:x:y



### 背包 3400
* 获取背包 3411
playId
* 返回背包 3412
种类数;goodId:num;goodId:num;goodId:num;
* 背包变化 3414
goodId:num 

* 放到市场 3421
playId:goodId:num
* 获取列表 3423
* 购买 3424
* 不卖了 3427
* 下架商品 3429


### 任务 3500
* 接主线任务 351
id:taskId:subTaskId
* 返回
id:taskId:subTaskId




## Redis结构(不需要自己建立,程序会自动建立)
* 玩家信息 n个 (hash结构), 每个玩家id作为key
* 所有聊天频道列表(sets结构) key为chatChannel value为各个频道的id
    保存所有聊天频道列表(包括默认,队伍,家族等)
* 默认公共聊天频道的成员列表(sets结构) n个 频道ID作为key 
    注意:不包括队伍和家族的,因为队伍和家族的可以直接从其成员列表中获取

* 所有队伍列表 team (sets结构) 保存所有队伍
* 每个队伍的成员列表(sorted sets结构) n个 key为team+id value为成员的id

* 场景列表 scene (sets结构)
* 每个场景的玩家列表 n个 key为scene+sceneId value为成员玩家id
