package com.newfly.mapper;

import com.newfly.common.MysqlUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LobbyMapper
{
    @Autowired
    private MysqlUtil mysqlUtil;

    // 登录
    public int queryByPlayer(String name, String password) {
        String query = "SELECT id from player where `name` = ? and `password` = ? limit 1";

        Connection conn = mysqlUtil.getConnection();
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, password);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (
                Exception e) {
            System.out.println("执行查询语句失败");
            e.printStackTrace();
        }
        return 0;
    }
}
