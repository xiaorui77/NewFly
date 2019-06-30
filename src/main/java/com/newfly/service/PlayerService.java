package com.newfly.service;

import com.newfly.common.ConstantDefine;
import com.newfly.common.SocketChannelMap;
import com.newfly.dao.PlayerRedis;
import com.newfly.dao.SceneRedis;
import com.newfly.pojo.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class PlayerService
{
    @Autowired
    PlayerRedis playerRedis;

    @Autowired
    SceneRedis sceneRedis;


    // 获取(所有)玩家信息
    public ResultMessage queryPlayer(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String method = strings[1];

        List<Map<String, String>> players = new ArrayList<>();

        if (method.equals("world")) {
            return null;
        } else if (method.equals("scene")) {
            // 获取场景内的所有玩家
            String scene = playerRedis.getScene(playerId);
            Set<String> member = sceneRedis.sceneMember(scene);
            // 获取所有信息
            for (String m : member) {
                Map<String, String> info = playerRedis.getInfo(m);
                players.add(info);
            }
        } else if (method.equals("team")) {
            return null;
        } else {
            Map<String, String> player = playerRedis.getInfo(method);
            players.add(player);
        }

        for (Map<String, String> p : players) {
            String playerString = p.get("id") + ":" + p.get("name") + ":" + p.get("profession") + ":" + p.get("grade");
            SocketChannelMap.sendTo(p.get("id"), ConstantDefine.MESSAGE_PLAYER_INFO_RETURN, playerString);
        }
        return null;
    }

}// end
