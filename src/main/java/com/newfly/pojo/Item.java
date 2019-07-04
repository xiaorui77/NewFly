package com.newfly.pojo;

public class Item
{
    long id;
    protected String name;  // 暂时没有这个功能
    int kind;   // 种类
    int backpackIndex;    // 背包位置

    private int num;    // 数量
    int price;  // 价格
    private int owner;  // 所属玩家


    public Item() {
    }

    // 通过字符串构造
    public Item(String item) {
        String[] strings = item.split(":");
        id = Long.parseLong(strings[0]);
        kind = Integer.parseInt(strings[1]);
        backpackIndex = Integer.parseInt(strings[2]);
        num = Integer.parseInt(strings[3]);
    }


    // 转换为返回结果
    public String toResultContent() {
        return id + ":" + kind + ":" + backpackIndex + ":" + num;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getBackpackIndex() {
        return backpackIndex;
    }

    public void setBackpackIndex(int backpackIndex) {
        this.backpackIndex = backpackIndex;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}
