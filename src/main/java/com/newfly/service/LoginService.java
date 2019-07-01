package com.newfly.service;


import com.newfly.mapper.LoginMapper;
import com.newfly.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService
{
    @Autowired
    LoginMapper loginMapper;


    // 用户登录验证, 成功返回playerId列表
    public User LoginValidation(String username, String password) {
        User user = new User(username, password);
        user = loginMapper.login(user);

        // 登录失败, 返回null
        if (user == null)
            return null;

        // 是否在其他地方登录检查


        // 登录成功!
        return user;
    }

    // 获取playerId列表
    public List<Integer> getPlayerIds(int id) {
        return loginMapper.queryPlayerIds(id);
    }

    // 选择角色
    public int selectCharacter(int userId, int charId) {
        // 选择角色
        List<Integer> list = loginMapper.queryPlayerIds(userId);
        if (!list.contains(charId))
            return 0;

        // 保存本次登录
        User user = new User();
        user.setId(userId);
        user.setId(charId);
        loginMapper.updateLastPlayer(user);
        return charId;
    }

}// end
