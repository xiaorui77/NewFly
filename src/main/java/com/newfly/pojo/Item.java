package com.newfly.pojo;

public class Item
{
    protected long id;
    protected String name;  // 暂时没有这个功能
    int kind;   // 种类
    int backpackIndex;    // 背包位置, 市场的时候当总价
    int owner;  // 所属玩家

    private int num;    // 数量


    Item() {
    }

    // 通过字符串构造
    public Item(String item) {
        String[] strings = item.split(":");
        id = Long.parseLong(strings[0]);
        name = strings[1];
        kind = Integer.parseInt(strings[2]);
        backpackIndex = Integer.parseInt(strings[3]);
        owner = Integer.parseInt(strings[4]);
        num = Integer.parseInt(strings[5]);
    }


    // 转换为返回结果字符串
    public String toResultContent() {
        return id + ":" + kind + ":" + backpackIndex + ":" + num;
    }

    // 转为市场结果字符串
    public String toCommodityContent() {
        return id + ":" + kind + ":" + backpackIndex + ":" + owner + ":" + num;
    }

    // 转换为保存字符串
    public String toSaveString() {
        return id + ":" + name + ":" + kind + ":" + backpackIndex + ":" + owner + ":" + num;
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

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}
