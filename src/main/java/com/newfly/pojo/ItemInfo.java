package com.newfly.pojo;

public class ItemInfo
{
    private int id;       // 物品种类
    private String name;
    private int grade;       // 限制使用等级
    private int price;       // 物品价格


    public int getId() {
        return id;
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


    public void setId(int id) {
        this.id = id;
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

}
