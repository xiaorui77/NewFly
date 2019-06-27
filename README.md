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

* 其他 2000
* 玩家 3000
* 战斗 5000



### 登录 3100
* 玩家登录 3101 
name:password
* 返回 3102
玩家信息(待完成)
* 玩家退出 3103
* 返回 无

* 注册 3201
* 注销 3203



### 聊天 3200
* 公共聊天 3211
id:channelId:message
* 返回 3212
channelId:fromId:message

* 私聊 3231
id:targetID:message
* 返回 3232
fromId:message
 


### 移动 3300
id:mapID:x:y



## 市场 3400
* 放到市场 3401
* 获取列表 3403
* 购买 3404
* 不卖了 3407
* 下架商品 3409



### 任务 3500
* 接主线任务 351
id:taskId:subTaskId
* 返回
id:taskId:subTaskId



### 副本 3600
* 进入副本



### 背包 3700
* 获取 371
* 返回背包 372
种类数;goodId:num;goodId:num;goodId:num;
* 背包变化 374
goodId:num 
