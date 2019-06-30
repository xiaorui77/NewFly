package com.newfly.common;

public class ConstantDefine
{
    // 3.用户,玩家 3000
    private static final int MESSAGE_PLAYER = 3000;
    // 玩家登录
    public static final int MESSAGE_PLAYER_LOGIN = MESSAGE_PLAYER + 111;
    public static final int MESSAGE_PLAYER_LOGIN_RETURN = MESSAGE_PLAYER + 112;
    // 玩家退出
    public static final int MESSAGE_PLAYER_LOGOUT = MESSAGE_PLAYER + 113;
    public static final int MESSAGE_PLAYER_LOGOUT_RETURN = MESSAGE_PLAYER + 114;

    // 获取玩家列表
    public static final int MESSAGE_PLAYER_QUERY = MESSAGE_PLAYER + 211;
    public static final int MESSAGE_PLAYER_QUERY_RETURN = MESSAGE_PLAYER + 212;
    // 玩家详细信息
    public static final int MESSAGE_PLAYER_INFO = MESSAGE_PLAYER + 213;
    public static final int MESSAGE_PLAYER_INFO_RETURN = MESSAGE_PLAYER + 214;


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


    // 6.保留 5000


    // 7.物品,背包,市场 7000


    // 8.任务 8000
    private static final int MESSAGE_TASK = 8000;
    // 任务阶段变化
    public static final int MESSAGE_TASK_CHANGE = MESSAGE_TASK + 111;
    public static final int MESSAGE_TASK_CHANGE_RETURN = MESSAGE_TASK + 112;
    // 获取任务列表
    public static final int MESSAGE_TASK_QUERY = MESSAGE_TASK + 121;
    public static final int MESSAGE_TASK_QUERY_RETURN = MESSAGE_TASK + 122;


}// end
