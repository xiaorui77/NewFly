package com.newfly.pojo;

public class Player
{
    private int id;
    private String name;
    private String password;
    private int profession; // 职业
    private int grade;  // 等级
    private int exp;    // 经验
    private int money;
    private int scene;  // 场景
    private int x;
    private int y;
    private String create_time;

    public Player() {
    }

    public Player(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
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

    public String getCreate_time() {
        return create_time;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", profession=" + profession +
                ", grade=" + grade +
                ", exp=" + exp +
                ", money=" + money +
                ", scene=" + scene +
                ", x=" + x +
                ", y=" + y +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
