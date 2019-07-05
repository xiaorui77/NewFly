package com.newfly.common;

public class Constant
{
    // 3.用户,玩家 3000
    private static final int PLAYER = 3000;
    // 用户登录
    public static final int USER_LOGIN = PLAYER + 111;
    public static final int USER_LOGIN_RETURN = PLAYER + 112;
    // 用户退出
    public static final int MESSAGE_USER_LOGOUT = PLAYER + 113;
    public static final int MESSAGE_USER_LOGOUT_RETURN = PLAYER + 114;

    // 选择玩家
    public static final int MESSAGE_PLAYER_SELECT = PLAYER + 121;
    public static final int MESSAGE_PLAYER_SELECT_RESULT = PLAYER + 122;

    // 退出玩家
    public static final int MESSAGE_PLAYER_QUIT = PLAYER + 123;
    public static final int MESSAGE_PLAYER_QUIT_RETURN = PLAYER + 124;


    // 获取玩家列表
    public static final int MESSAGE_PLAYER_QUERY = PLAYER + 211;
    public static final int MESSAGE_PLAYER_QUERY_RETURN = PLAYER + 212;
    // 玩家详细信息
    public static final int MESSAGE_PLAYER_INFO = PLAYER + 213;
    public static final int MESSAGE_PLAYER_INFO_RETURN = PLAYER + 214;


    // 4.聊天,队伍等 4000
    private static final int MESSAGE_CHAT = 4000;
    // 好友聊天
    public static final int MESSAGE_CHAT_PRIVATE = MESSAGE_CHAT + 111;
    public static final int MESSAGE_CHAT_PRIVATE_RETURN = MESSAGE_CHAT + 112;
    // 公共聊天
    public static final int MESSAGE_CHAT_PUBLIC = MESSAGE_CHAT + 121;
    public static final int MESSAGE_CHAT_PUBLIC_RETURN = MESSAGE_CHAT + 122;

    // 创建队伍
    public static final int MESSAGE_TEAM_CREATE = MESSAGE_CHAT + 211;
    public static final int MESSAGE_TEAM_CREATE_RETURN = MESSAGE_CHAT + 212;
    // 查询队伍
    public static final int MESSAGE_TEAM_QUERY = MESSAGE_CHAT + 213;
    public static final int MESSAGE_TEAM_QUERY_RETURN = MESSAGE_CHAT + 214;
    // 加入队伍
    public static final int MESSAGE_TEAM_JOIN = MESSAGE_CHAT + 215;
    public static final int MESSAGE_TEAM_JOIN_RETURN = MESSAGE_CHAT + 216;
    // 退出队伍
    public static final int MESSAGE_TEAM_QUIT = MESSAGE_CHAT + 217;
    public static final int MESSAGE_TEAM_QUIT_RETURN = MESSAGE_CHAT + 218;


    // 5.玩家移动,场景,副本等 5000
    private static final int MESSAGE_MAP = 5000;
    // 玩家移动
    public static final int MESSAGE_MAP_PLAYER_MOVE = MESSAGE_MAP + 111;
    public static final int MESSAGE_MAP_PLAYER_MOVE_RETURN = MESSAGE_MAP + 112;


    // 6.战斗 6000
    private static final int BATTLE = 6000;
    public static final int BATTLE_SINGLE = BATTLE + 111;
    public static final int BATTLE_SINGLE_RETURN = BATTLE + 112;
    // 技能选择
    public static final int BATTLE_ABILITY_SELECT = BATTLE + 113;
    public static final int BATTLE_ABILITY_SELECT_RETURN = BATTLE + 114;
    // 状态变化
    public static final int BATTLE_STATUS_CHANGE_RETURN = BATTLE + 116;
    // 战斗结果
    public static final int BATTLE_RESULT_RETURN = BATTLE + 118;


    // 7.物品,背包,市场 7000
    private static final int BACKPACK = 7000;
    // 获取物品/装备详情
    public static final int BACKPACK_ITEM_INFO = BACKPACK + 111;
    public static final int BACKPACK_ITEM_INFO_RETURN = BACKPACK + 112;
    // 使用物品
    public static final int BACKPACK_ITEM_USE = BACKPACK + 113;
    public static final int BACKPACK_ITEM_USE_RETURN = BACKPACK + 114;
    // 穿戴/脱下装备
    public static final int BACKPACK_EQUIPMENT_WEAR = BACKPACK + 115;
    public static final int BACKPACK_EQUIPMENT_WEAR_RETURN = BACKPACK + 116;
    // 物品变化
    public static final int BACKPACK_ITEM_CHANGE_RETURN = BACKPACK + 116;

    // 查询背包信息
    public static final int BACKPACK_INFO = BACKPACK + 211;
    public static final int BACKPACK_INFO_RETURN = BACKPACK + 212;
    // 获取背包/身上物品
    public static final int BACKPACK_ITEM = BACKPACK + 213;
    public static final int BACKPACK_ITEM_RETURN = BACKPACK + 214;


    // 8.任务 8000
    private static final int MESSAGE_TASK = 8000;
    // 任务阶段变化
    public static final int MESSAGE_TASK_CHANGE = MESSAGE_TASK + 111;
    public static final int MESSAGE_TASK_CHANGE_RETURN = MESSAGE_TASK + 112;
    // 获取任务列表
    public static final int MESSAGE_TASK_QUERY = MESSAGE_TASK + 121;
    public static final int MESSAGE_TASK_QUERY_RETURN = MESSAGE_TASK + 122;


}// end
