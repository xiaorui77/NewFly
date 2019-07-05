package com.newfly.service;


import com.newfly.dao.BackpackRedis;
import com.newfly.dao.PlayerRedis;
import com.newfly.mapper.BackpackMapper;
import com.newfly.mapper.LoginMapper;
import com.newfly.mapper.PlayerMapper;
import com.newfly.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService
{
    @Autowired
    LoginMapper loginMapper;

    @Autowired
    PlayerMapper playerMapper;

    @Autowired
    PlayerRedis playerRedis;

    @Autowired
    BackpackMapper backpackMapper;

    @Autowired
    BackpackRedis backpackRedis;


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
    public Player selectCharacter(int userId, int charId) {
        // 选择角色
        List<Integer> list = loginMapper.queryPlayerIds(userId);
        if (!list.contains(charId))
            return null;

        // 获取选择的角色信息并保存到Redis
        Player player = playerMapper.queryPlayer(charId);
        Task task = playerMapper.queryTask(charId);
        Combat combat = playerMapper.queryCombat(charId);
        playerRedis.savePlayer(player);
        playerRedis.saveMainTask(task);
        playerRedis.setCombat(combat);
        List<Item> items = backpackMapper.queryItems(charId);
        items.addAll(backpackMapper.queryEquipment(charId));
        items.addAll(backpackMapper.queryWearing(charId));
        backpackRedis.setCarryAll(String.valueOf(charId), items);

        return player;
    }


    // 获取上次登录
    public Integer queryLastPlayer(int userId) {
        return loginMapper.queryLastPlayer(userId);
    }

    // 更新上次登录
    public void updateLastPlayer(int userId, int playerId) {
        // 保存本次登录
        User user = new User();
        user.setId(userId);
        user.setId(playerId);
        loginMapper.updateLastPlayer(user);
    }

    // 保存redis中player信息到mysql
    public void savePlayerToMysql(String playerId) {
        Player player = playerRedis.getPlayer(playerId);
        Task mainTask = playerRedis.getMainTask(playerId);
        Combat combat = playerRedis.getCombat(playerId);

        playerMapper.updatePlayer(player);
        playerMapper.updateMainTask(mainTask);
        playerMapper.updateCombat(combat);
    }

}// end
