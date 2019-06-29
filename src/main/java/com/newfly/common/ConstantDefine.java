package com.newfly.common;

public class ConstantDefine
{
    // 登录 3100
    private static final int MESSAGE_PLAYER = 3100;
    // 玩家登录
    public static final int MESSAGE_PLAYER_LOGIN = MESSAGE_PLAYER + 11;
    public static final int MESSAGE_PLAYER_LOGIN_RETURN = MESSAGE_PLAYER + 12;
    // 玩家退出
    public static final int MESSAGE_PLAYER_LOGOUT = MESSAGE_PLAYER + 13;
    public static final int MESSAGE_PLAYER_LOGOUT_RETURN = MESSAGE_PLAYER + 14;


    // 队伍,家族,聊天等 3200
    private static final int MESSAGE_CHAT = 3200;
    // 好友聊天
    public static final int MESSAGE_CHAT_PRIVATE = MESSAGE_CHAT + 11;
    public static final int MESSAGE_CHAT_PRIVATE_RETURN = MESSAGE_CHAT + 12;
    // 公共聊天
    public static final int MESSAGE_CHAT_PUBLIC = MESSAGE_CHAT + 21;
    public static final int MESSAGE_CHAT_PUBLIC_RETURN = MESSAGE_CHAT + 22;
    // 创建队伍
    public static final int MESSAGE_TEAM_CREATE = MESSAGE_CHAT + 31;
    public static final int MESSAGE_TEAM_CREATE_RETURN = MESSAGE_CHAT + 32;
    // 查询队伍
    public static final int MESSAGE_TEAM_QUERY = MESSAGE_CHAT + 33;
    public static final int MESSAGE_TEAM_QUERY_RETUREN = MESSAGE_CHAT + 34;
    // 加入队伍
    public static final int MESSAGE_TEAM_JOIN = MESSAGE_CHAT + 35;
    public static final int MESSAGE_TEAM_JOIN_RETURN = MESSAGE_CHAT + 36;
    // 退出队伍
    public static final int MESSAGE_TEAM_QUIT = MESSAGE_CHAT + 37;
    public static final int MESSAGE_TEAM_QUIT_RETURN = MESSAGE_CHAT + 38;


    // 场景,玩家移动等
    private static final int MESSAGE_MAP = 3300;
    // 玩家移动
    public static final int MESSAGE_MAP_PLAYER_MOVE = 3311;
    public static final int MESSAGE_MAP_PLAYER_MOVE_RETURN = 3312;

}// end
