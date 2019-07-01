package com.newfly.pojo;

public class User
{
    private int id;
    private String username;
    private String password;
    private int lastPlayer;
    private int playerNum;
    private String createTime;


    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getLastPlayer() {
        return lastPlayer;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public String getCreateTime() {
        return createTime;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastPlayer(int lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
