package com.newfly.pojo;

public class Goods
{
    private int id;
    private int kind;       //0代表一般物品,1代表武器，2代表衣服
    private String name;
    private int grade;       // 等级
    private int price;       //物品价格
    private String owner;    // 所有者id


    public int getId() {
        return id;
    }

    public int getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    public int getPrice() {
        return price;
    }

    public String getOwner() {
        return owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
