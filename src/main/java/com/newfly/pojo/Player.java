package com.newfly.pojo;

public class Player
{
    private int id;
    private String name;
    private int userId;
    private int profession; // 职业
    private int grade;  // 等级
    private int exp;    // 经验
    private int money;
    private int scene;  // 场景
    private int x;
    private int y;
    private String createTime;

    public Player() {
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    public int getProfession() {
        return profession;
    }

    public int getGrade() {
        return grade;
    }

    public int getExp() {
        return exp;
    }

    public int getMoney() {
        return money;
    }

    public int getScene() {
        return scene;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getCreateTime() {
        return createTime;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProfession(int profession) {
        this.profession = profession;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setScene(int scene) {
        this.scene = scene;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    // 转为字符串类型
    public String toResult() {
        return id + ":" + name + ":" + profession + ":" + grade + ":" + exp + ":" + money + ":" + scene + ":" + x + ":" + y;
    }
}
