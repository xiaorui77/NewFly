package com.newfly.service;


import com.newfly.common.Constant;
import com.newfly.common.SocketChannelMap;
import com.newfly.dao.PlayerRedis;
import com.newfly.dao.SceneRedis;
import com.newfly.dao.TeamRedis;
import com.newfly.pojo.ResultMessage;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


@Service
public class TeamService
{
    @Autowired
    TeamRedis teamRedis;

    @Autowired
    PlayerRedis playerRedis;

    @Autowired
    SceneRedis sceneRedis;


    // 创建队伍
    public ResultMessage createTeam(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];


        // 是否可以创建队伍
        String team = playerRedis.getTeam(playerId);
        if (team != null && Integer.parseInt(team) >= 10000)
            return null;

        // 创建队伍
        String teamId = teamRedis.createTeam(playerId);
        if (teamId == null)
            return null;
        return new ResultMessage(Constant.MESSAGE_TEAM_CREATE_RETURN, teamId);
    }

    // 查询所有队伍信息
    public ResultMessage queryTeam(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String method = strings[1];

        Set<String> teams;
        // 查询队伍列表
        if (method.equals("world")) {
            teams = teamRedis.worldTeam();
        } else if (method.equals("scene")) {
            teams = teamRedis.sceneTeam(playerRedis.getScene(playerId));
        } else if (Pattern.compile("[0-9]*").matcher(method).matches()) {
            // 查询单个team信息
            teams = new HashSet<>();
            teams.add(method);
        } else {
            return null;
        }

        // 查询队伍详情并发送
        Channel channel = SocketChannelMap.get(playerId);
        if (channel == null)
            return null;
        for (String t : teams) {
            Map<String, String> teamInfo = teamRedis.teamInfo(t);
            Map<String, String> captainInfo = playerRedis.getInfo(teamInfo.get("captainId"));
            int memberNum = Integer.valueOf(teamInfo.get("memberNum"));
            channel.write(new ResultMessage(Constant.MESSAGE_TEAM_QUERY_RETURN, t + ":" + teamInfo.get("name") + ":" + teamInfo.get("captainId") + ":" + captainInfo.get("grade") + ":" + captainInfo.get("profession") + ":" + (memberNum + 1)));
        }
        channel.flush();
        return null;
    }

    // 加入队伍
    public ResultMessage joinTeam(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];
        String teamId = strings[1];

        // 是否可以加入队伍
        String team = playerRedis.getTeam(playerId);
        if (team != null && Integer.parseInt(team) >= 10000)
            return null;

        // 加入队伍 失败直接返回
        if (!teamRedis.joinTeam(teamId, playerId))
            return null;
        playerRedis.joinTeam(playerId, teamId);

        // 获取成员信息并返回
        String memberString = teamRedis.memberString(teamId);
        Set<String> teamMember = teamRedis.teamMember(teamId);
        // 给所有成员广播
        SocketChannelMap.sendAll(teamMember, Constant.MESSAGE_TEAM_JOIN_RETURN, teamId + ":" + memberString);
        return null;
    }

    // 退出队伍
    public ResultMessage quitTeam(ResultMessage msg) {
        // 分离数据
        String[] strings = msg.getBody().split(":");
        String playerId = strings[0];

        String teamId = playerRedis.getTeam(playerId);

        // 如果是队长
        if (playerRedis.isCaptain(playerId)) {
            // 队长离开队伍
            if (teamRedis.transfarCaptain(playerId, teamId)) {
                // 若还有成员, 先修改队伍名称
                String name = playerRedis.getName(playerId);
                teamRedis.rename(teamId, name);

                // 给剩余的队长和队员发送信息
                String memberString = teamRedis.memberString(teamId);
                Set<String> teamMember = teamRedis.teamMember(teamId);
                SocketChannelMap.sendAll(teamMember, Constant.MESSAGE_TEAM_JOIN_RETURN, memberString);
            } else {
                String sceneId = playerRedis.getScene(playerId);
                // 世界和场景删除该队伍
                sceneRedis.removeTeam(teamId, sceneId);
            }

            // player 结构中删除 返回成功信息,只需要teamId即可
            playerRedis.leaveTeam(playerId);
            return new ResultMessage(Constant.MESSAGE_TEAM_QUIT_RETURN, teamId);
        }

        // 如果是普通成员
        teamRedis.quitTeam(playerId, teamId);
        // player 结构中删除
        playerRedis.leaveTeam(playerId);

        // 给其他成员发送 变动列表
        String teamMember = teamRedis.memberString(teamId);
        Set<String> all = teamRedis.teamMember(teamId);
        all.remove(playerId);
        for (String m : all) {
            Channel channel = SocketChannelMap.get(m);
            channel.writeAndFlush(new ResultMessage(Constant.MESSAGE_TEAM_JOIN_RETURN, teamId + ":" + teamMember));
        }
        // 给自己发送成功 只需要teamId即可
        return new ResultMessage(Constant.MESSAGE_TEAM_QUIT_RETURN, teamId);
    }

}// end
