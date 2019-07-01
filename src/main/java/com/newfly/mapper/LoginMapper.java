package com.newfly.mapper;

import com.newfly.pojo.User;

import java.util.List;

public interface LoginMapper
{
    // 登录验证
    User login(User user);

    // 获取用户的playerId列表
    List<Integer> queryPlayerIds(int id);

    // 设置上次选择的player
    void updateLastPlayer(User user);

}
