package com.newfly.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class TeamRedis
{
    @Autowired
    private JedisPool jedisPool;

    // 当前分配的队伍ID 10000-100000
    private int curTeamID = 10000;

    // 创建队伍
    public String createTeam(String playerId) {
        Jedis jedis = jedisPool.getResource();

        // 获取队长name
        String teamId = String.valueOf(curTeamID++);
        Map<String, String> captain = jedis.hgetAll("player:" + playerId);
        if (captain == null) {
            jedis.close();
            return null;
        }

        Map<String, String> team = new HashMap<>();
        team.put("name", captain.get("name") + " 的队伍");
        team.put("captainId", playerId);
        team.put("memberNum", String.valueOf(0));

        // 添加队伍详情列表
        // 添加到世界和场景队伍列表
        jedis.hset("team:" + teamId, team);
        jedis.sadd("scene_team:" + captain.get("scene"), teamId);
        jedis.sadd("world_team", teamId);

        // 给玩家添加队伍
        jedis.hset("player:" + playerId, "team", teamId);
        jedis.close();
        return teamId;
    }

    // 查询世界队伍
    public Set<String> worldTeam() {
        Jedis jedis = jedisPool.getResource();

        // 获取所有队伍列表
        Set<String> teams = jedis.smembers("world_team");
        jedis.close();
        return teams;
    }

    // 查询某场景队伍
    public Set<String> sceneTeam(String sceneId) {
        Jedis jedis = jedisPool.getResource();

        // 获取场景的队伍列表
        Set<String> teams = jedis.smembers("scene_team:" + sceneId);
        jedis.close();
        return teams;
    }

    // 查询某队伍详情
    public Map<String, String> teamInfo(String teamId) {
        Jedis jedis = jedisPool.getResource();

        Map<String, String> teamInfo = jedis.hgetAll("team:" + teamId);
        jedis.close();
        return teamInfo;
    }


    // 加入队伍 仅仅修改team中的信息
    public boolean joinTeam(String teamId, String memberId) {
        Jedis jedis = jedisPool.getResource();
        // 添加到队伍详情列表中
        for (int i = 1; i < 3; i++) {
            if (jedis.hsetnx("team:" + teamId, "member" + i, memberId) == 1) {
                jedis.hincrBy("team:" + teamId, "memberNum", 1);
                jedis.close();
                return true;
            }
        }
        jedis.close();
        return false;
    }

    // 获取队伍成员, 包括队长
    public Set<String> teamMember(String teamId) {
        Jedis jedis = jedisPool.getResource();

        // 添加队长
        Set<String> member = new HashSet<>();
        member.add(jedis.hget("team:" + teamId, "captainId"));

        // 添加成员1
        String member1 = jedis.hget("team:" + teamId, "member1");
        if (member1 == null) {
            jedis.close();
            return member;
        }
        member.add(member1);

        // 添加成员2
        String member2 = jedis.hget("team:" + teamId, "member2");
        if (member2 == null) {
            jedis.close();
            return member;
        }
        member.add(member2);

        jedis.close();
        return member;
    }

    // 获取队伍第一个成员
    public String member1(String teamId) {
        Jedis jedis = jedisPool.getResource();
        String member1 = jedis.hget("team:" + teamId, "member1");
        jedis.close();
        return member1;
    }

    // 队长离开队伍, 返回是否离开成功
    public boolean transfarCaptain(String playerId, String teamId) {
        Jedis jedis = jedisPool.getResource();
        // 删除captainId并查询是否还有member
        jedis.hdel("team:" + teamId, "captainId");
        String member1 = jedis.hget("team:" + teamId, "member1");
        // 如果没有成员则解散该队伍
        if (member1 == null) {
            jedis.del("team:" + teamId);
            jedis.close();
            return false;
        }
        // 否则member1上位
        jedis.hset("team:" + teamId, "captainId", member1);
        jedis.hincrBy("team:" + teamId, "memberNum", -1);
        // member2 也上位
        String member2 = jedis.hget("team:" + teamId, "member2");
        if (member2 == null) {
            jedis.close();
            return true;
        }

        jedis.hset("team:" + teamId, "member1", member2);
        jedis.hdel("team:" + teamId, "member2");
        jedis.close();
        return true;
    }

    // 离开队伍 (成员)
    public void quitTeam(String playerId, String teamId) {
        Jedis jedis = jedisPool.getResource();

        int num = Integer.parseInt(jedis.hget("team:" + teamId, "memberNum"));
        if (num == 0) {
            jedis.close();
            return;
        } else if (num == 1) {
            jedis.hdel("team:" + teamId, "member1");
        } else {
            String member1 = jedis.hget("team:" + teamId, "member1");
            String member2 = jedis.hget("team:" + teamId, "member2");
            // 如果删除的是第一个
            if (member1.equals(playerId)) {
                jedis.hset("team:" + teamId, "member1", member2);
            }
            jedis.hdel("team:" + teamId, "member2");
        }
        jedis.hincrBy("team:" + teamId, "memberNum", -1);
        jedis.close();
    }

    // 获取成员数
    public int memberNum(String teamId) {
        Jedis jedis = jedisPool.getResource();
        int num = Integer.parseInt(jedis.hget("team:" + teamId, "memberNum"));
        jedis.close();
        return num;
    }

    // 获取所有成员string captainId:member1:member2
    public String memberString(String teamId) {
        Jedis jedis = jedisPool.getResource();
        String memberString = jedis.hget("team:" + teamId, "captainId");

        String member1 = jedis.hget("team:" + teamId, "member1");
        if (member1 == null) {
            jedis.close();
            return memberString + ":0:0";
        }
        memberString += ":" + member1;

        String member2 = jedis.hget("team:" + teamId, "member2");
        if (member2 == null) {
            jedis.close();
            return memberString + ":0";
        }
        jedis.close();
        return memberString + ":" + member2;
    }

    // 获取队长id
    public String captain(String teamId) {
        Jedis jedis = jedisPool.getResource();
        String captain = jedis.hget("team:" + teamId, "captainId");
        jedis.close();
        return captain;
    }

    // 修改队伍名称
    public void rename(String teamId, String name) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("team:" + teamId, "name", name);
        jedis.close();
    }

}// end
