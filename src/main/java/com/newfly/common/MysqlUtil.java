package com.newfly.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class MysqlUtil
{
    private static final String url = "jdbc:mysql://192.168.22.137/newfly";
    private static final String user = "newfly";
    private static final String password = "newfly";

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet resultSet = null;

    public MysqlUtil(String url, String user, String password) {
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("数据库连接失败！");
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return this.conn;
    }


    /* 执行SQL语句:查询并返回结果, 这是单结果返回，我们测试的时候就是根据条件查询，然后返回一个结果与期望结果比较即可，所以单结果足已*/
    public String getData(String sql, String targetName) {
        String result = null;
        try {

            pst = conn.prepareStatement(sql);
            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getString(targetName);
            }
        } catch (Exception e) {
            System.out.println("执行查询语句失败");
            e.printStackTrace();
        }
        return result;
    }

    // 关闭链接
    private void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (Exception e) {
            System.out.println("关闭数据库连接失败！");
            e.printStackTrace();
        }
    }

}
